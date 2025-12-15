package Sekwang.Service;

import Sekwang.Domain.MinecraftPlayerLog;
import Sekwang.Domain.MinecraftPlayerLog.EventType;
import Sekwang.Domain.MinecraftEvent;
import Sekwang.Repository.MinecraftPlayerLogRepository;
import Sekwang.Repository.MinecraftEventRepository;
import Sekwang.api.DTO.MinecraftDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MinecraftService {

    private final MinecraftPlayerLogRepository logRepository;
    private final MinecraftEventRepository eventRepository;

    @Value("${minecraft.server.host:localhost}")
    private String serverHost;

    @Value("${minecraft.server.port:19132}")
    private int serverPort;

    // 현재 접속 중인 플레이어 추적 (메모리)
    private Set<String> currentOnlinePlayers = ConcurrentHashMap.newKeySet();

    // 플레이어별 접속 시간 기록
    private Map<String, LocalDateTime> playerJoinTimes = new ConcurrentHashMap<>();

    private static final byte[] UNCONNECTED_PING = new byte[] {
            0x01,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, (byte) 0xff, (byte) 0xff, 0x00, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe,
            (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, 0x12, 0x34, 0x56, 0x78,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };

    public MinecraftService(MinecraftPlayerLogRepository logRepository, MinecraftEventRepository eventRepository) {
        this.logRepository = logRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * 베드락 서버 상태 조회 (RakNet 프로토콜)
     */
    public MinecraftDto.ServerStatus getServerStatus() {
        long startTime = System.currentTimeMillis();

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(3000);

            InetAddress address = InetAddress.getByName(serverHost);

            byte[] pingPacket = createPingPacket();
            DatagramPacket sendPacket = new DatagramPacket(pingPacket, pingPacket.length, address, serverPort);
            socket.send(sendPacket);

            byte[] receiveData = new byte[4096];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            long latency = System.currentTimeMillis() - startTime;

            return parseServerStatus(receivePacket.getData(), receivePacket.getLength(), latency);

        } catch (SocketTimeoutException e) {
            log.warn("Minecraft server timeout: {}", e.getMessage());
            return createOfflineStatus("Server timeout");
        } catch (Exception e) {
            log.error("Error querying Minecraft server: {}", e.getMessage());
            return createOfflineStatus("Connection error");
        }
    }

    public MinecraftDto.PlayerList getPlayers() {
        MinecraftDto.ServerStatus status = getServerStatus();

        if (!status.isOnline()) {
            return new MinecraftDto.PlayerList(false, 0, new ArrayList<>());
        }

        return new MinecraftDto.PlayerList(
                true,
                status.getCurrentPlayers(),
                new ArrayList<>(currentOnlinePlayers));
    }

    // ============ 로그 관련 메소드 ============

    /**
     * 최근 로그 조회
     */
    public List<MinecraftDto.PlayerLogResponse> getRecentLogs() {
        return logRepository.findTop50ByOrderByEventTimeDesc().stream()
                .map(this::toLogResponse)
                .collect(Collectors.toList());
    }

    /**
     * 특정 플레이어의 로그 조회
     */
    public List<MinecraftDto.PlayerLogResponse> getPlayerLogs(String playerName) {
        return logRepository.findByPlayerNameOrderByEventTimeDesc(playerName).stream()
                .map(this::toLogResponse)
                .collect(Collectors.toList());
    }

    /**
     * 오늘의 통계
     */
    public MinecraftDto.DailyStats getDailyStats() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();

        Long uniquePlayers = logRepository.countUniquePlayersToday(startOfDay);
        List<MinecraftPlayerLog> todayLogs = logRepository.findByEventTimeBetweenOrderByEventTimeDesc(
                startOfDay, LocalDateTime.now());

        long totalSessions = todayLogs.stream()
                .filter(l -> l.getEventType() == EventType.JOIN)
                .count();

        List<MinecraftDto.PlayerLogResponse> recentLogs = todayLogs.stream()
                .limit(20)
                .map(this::toLogResponse)
                .collect(Collectors.toList());

        return new MinecraftDto.DailyStats(
                uniquePlayers != null ? uniquePlayers : 0,
                totalSessions,
                recentLogs);
    }

    /**
     * 플레이 시간 랭킹
     */
    public List<MinecraftDto.PlayTimeRanking> getPlayTimeRanking() {
        List<Object[]> rankings = logRepository.getPlayTimeRanking();
        List<MinecraftDto.PlayTimeRanking> result = new ArrayList<>();

        int rank = 1;
        for (Object[] row : rankings) {
            String playerName = (String) row[0];
            Long totalMinutes = ((Number) row[1]).longValue();

            result.add(new MinecraftDto.PlayTimeRanking(
                    rank++,
                    playerName,
                    totalMinutes,
                    formatPlayTime(totalMinutes)));
        }

        return result;
    }

    /**
     * 수동으로 플레이어 JOIN 이벤트 기록 (테스트/관리용)
     */
    @Transactional
    public void recordPlayerJoin(String playerName) {
        if (currentOnlinePlayers.add(playerName)) {
            playerJoinTimes.put(playerName, LocalDateTime.now());

            MinecraftPlayerLog logEntry = MinecraftPlayerLog.builder()
                    .playerName(playerName)
                    .eventType(EventType.JOIN)
                    .eventTime(LocalDateTime.now())
                    .serverAddress(serverHost)
                    .build();

            logRepository.save(logEntry);
            log.info("Player joined: {}", playerName);
        }
    }

    /**
     * 수동으로 플레이어 LEAVE 이벤트 기록 (테스트/관리용)
     */
    @Transactional
    public void recordPlayerLeave(String playerName) {
        if (currentOnlinePlayers.remove(playerName)) {
            LocalDateTime joinTime = playerJoinTimes.remove(playerName);
            long sessionMinutes = 0;

            if (joinTime != null) {
                sessionMinutes = ChronoUnit.MINUTES.between(joinTime, LocalDateTime.now());
            }

            MinecraftPlayerLog logEntry = MinecraftPlayerLog.builder()
                    .playerName(playerName)
                    .eventType(EventType.LEAVE)
                    .eventTime(LocalDateTime.now())
                    .sessionDurationMinutes(sessionMinutes)
                    .serverAddress(serverHost)
                    .build();

            logRepository.save(logEntry);
            log.info("Player left: {} (session: {} min)", playerName, sessionMinutes);
        }
    }

    /**
     * 현재 온라인 플레이어 목록 반환
     */
    public Set<String> getCurrentOnlinePlayers() {
        return new HashSet<>(currentOnlinePlayers);
    }

    // ============ Private 헬퍼 메소드 ============

    private MinecraftDto.PlayerLogResponse toLogResponse(MinecraftPlayerLog log) {
        return new MinecraftDto.PlayerLogResponse(
                log.getId(),
                log.getPlayerName(),
                log.getEventType().name(),
                log.getEventTime(),
                log.getSessionDurationMinutes());
    }

    private String formatPlayTime(long minutes) {
        long hours = minutes / 60;
        long mins = minutes % 60;

        if (hours > 0) {
            return String.format("%d시간 %d분", hours, mins);
        }
        return String.format("%d분", mins);
    }

    private byte[] createPingPacket() {
        byte[] packet = Arrays.copyOf(UNCONNECTED_PING, UNCONNECTED_PING.length);
        long time = System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.wrap(packet, 1, 8);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(time);
        new Random().nextBytes(Arrays.copyOfRange(packet, 25, 33));
        return packet;
    }

    private MinecraftDto.ServerStatus parseServerStatus(byte[] data, int length, long latency) {
        try {
            if (data[0] != 0x1c) {
                return createOfflineStatus("Invalid response");
            }

            int stringLength = ((data[33] & 0xFF) << 8) | (data[34] & 0xFF);
            String serverInfo = new String(data, 35, Math.min(stringLength, length - 35), "UTF-8");

            String[] parts = serverInfo.split(";");

            if (parts.length >= 6) {
                return new MinecraftDto.ServerStatus(
                        true,
                        parts[1],
                        parts.length > 3 ? parts[3] : "Unknown",
                        Integer.parseInt(parts[4]),
                        Integer.parseInt(parts[5]),
                        serverHost,
                        serverPort,
                        latency);
            }

            return createOfflineStatus("Parse error");

        } catch (Exception e) {
            log.error("Error parsing server response: {}", e.getMessage());
            return createOfflineStatus("Parse error");
        }
    }

    private MinecraftDto.ServerStatus createOfflineStatus(String reason) {
        return new MinecraftDto.ServerStatus(
                false,
                reason,
                null,
                0,
                0,
                serverHost,
                serverPort,
                -1);
    }

    // ============ 이벤트 관련 메소드 ============

    /**
     * 이벤트 기록
     */
    @Transactional
    public void recordEvent(String eventType, String playerName, String message) {
        try {
            MinecraftEvent event = MinecraftEvent.builder()
                    .eventType(eventType)
                    .playerName(playerName)
                    .message(message)
                    .eventTime(LocalDateTime.now())
                    .serverAddress(serverHost)
                    .build();

            if (eventRepository != null) {
                eventRepository.save(event);
            }
            log.info("Event recorded: {} - {} - {}", eventType, playerName, message);
        } catch (Exception e) {
            log.warn("Failed to record event: {}", e.getMessage());
        }
    }

    /**
     * 최근 이벤트 조회
     */
    public List<MinecraftDto.EventResponse> getRecentEvents() {
        try {
            if (eventRepository == null)
                return List.of();

            return eventRepository.findAllByOrderByEventTimeDesc(
                    org.springframework.data.domain.PageRequest.of(0, 50)).stream()
                    .map(this::toEventResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("Failed to get events: {}", e.getMessage());
            return List.of();
        }
    }

    private MinecraftDto.EventResponse toEventResponse(MinecraftEvent event) {
        return new MinecraftDto.EventResponse(
                event.getId(),
                event.getEventType(),
                event.getPlayerName(),
                event.getMessage(),
                event.getEventTime());
    }
}
