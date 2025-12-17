package Sekwang.Service;

import Sekwang.Domain.MinecraftPlayerLog;
import Sekwang.Domain.MinecraftPlayerLog.EventType;
import Sekwang.Domain.MinecraftEvent;
import Sekwang.Repository.MinecraftPlayerLogRepository;
import Sekwang.Repository.MinecraftEventRepository;
import Sekwang.api.DTO.MinecraftDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
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

    @Value("${minecraft.server.port.bedrock:19132}")
    private int bedrockPort;

    @Value("${minecraft.server.port.java:25565}")
    private int javaPort;

    // 현재 접속 중인 플레이어 추적 (메모리)
    private Set<String> currentOnlinePlayers = ConcurrentHashMap.newKeySet();

    // 플레이어별 접속 시간 기록
    private Map<String, LocalDateTime> playerJoinTimes = new ConcurrentHashMap<>();

    // Bedrock RakNet 프로토콜 패킷
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
     * 서버 상태 조회 - Java Edition과 Bedrock Edition 모두 시도
     */
    public MinecraftDto.ServerStatus getServerStatus() {
        // 먼저 Java Edition 시도 (TCP 25565)
        MinecraftDto.ServerStatus javaStatus = getJavaServerStatus();
        if (javaStatus.isOnline()) {
            log.debug("Java Edition server is online");
            return javaStatus;
        }

        // Java Edition이 오프라인이면 Bedrock Edition 시도 (UDP 19132)
        MinecraftDto.ServerStatus bedrockStatus = getBedrockServerStatus();
        if (bedrockStatus.isOnline()) {
            log.debug("Bedrock Edition server is online");
            return bedrockStatus;
        }

        // 둘 다 오프라인
        log.warn("Both Java and Bedrock servers are offline");
        return createOfflineStatus("Server offline", javaPort);
    }

    /**
     * Java Edition 서버 상태 조회 (SLP - Server List Ping, TCP)
     */
    private MinecraftDto.ServerStatus getJavaServerStatus() {
        long startTime = System.currentTimeMillis();

        try (Socket socket = new Socket()) {
            socket.setSoTimeout(3000);
            socket.connect(new InetSocketAddress(serverHost, javaPort), 3000);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            // Handshake 패킷
            ByteArrayOutputStream handshake = new ByteArrayOutputStream();
            DataOutputStream handshakeData = new DataOutputStream(handshake);
            writeVarInt(handshakeData, 0x00); // Packet ID
            writeVarInt(handshakeData, 767); // Protocol version (1.21.4)
            writeString(handshakeData, serverHost);
            handshakeData.writeShort(javaPort);
            writeVarInt(handshakeData, 1); // Next state: Status

            byte[] handshakeBytes = handshake.toByteArray();
            writeVarInt(out, handshakeBytes.length);
            out.write(handshakeBytes);

            // Status Request 패킷
            writeVarInt(out, 1); // Packet length
            writeVarInt(out, 0x00); // Packet ID

            out.flush();

            // Status Response 읽기
            int responseLength = readVarInt(in);
            int packetId = readVarInt(in);

            if (packetId != 0x00) {
                return createOfflineStatus("Invalid response", javaPort);
            }

            String json = readString(in, responseLength);
            long latency = System.currentTimeMillis() - startTime;

            return parseJavaServerStatus(json, latency);

        } catch (SocketTimeoutException e) {
            log.debug("Java server timeout: {}", e.getMessage());
            return createOfflineStatus("Java server timeout", javaPort);
        } catch (Exception e) {
            log.debug("Java server error: {}", e.getMessage());
            return createOfflineStatus("Java server offline", javaPort);
        }
    }

    /**
     * Bedrock Edition 서버 상태 조회 (RakNet 프로토콜, UDP)
     */
    private MinecraftDto.ServerStatus getBedrockServerStatus() {
        long startTime = System.currentTimeMillis();

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(3000);

            InetAddress address = InetAddress.getByName(serverHost);

            byte[] pingPacket = createPingPacket();
            DatagramPacket sendPacket = new DatagramPacket(pingPacket, pingPacket.length, address, bedrockPort);
            socket.send(sendPacket);

            byte[] receiveData = new byte[4096];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            long latency = System.currentTimeMillis() - startTime;

            return parseBedrockServerStatus(receivePacket.getData(), receivePacket.getLength(), latency);

        } catch (SocketTimeoutException e) {
            log.debug("Bedrock server timeout: {}", e.getMessage());
            return createOfflineStatus("Bedrock server timeout", bedrockPort);
        } catch (Exception e) {
            log.debug("Bedrock server error: {}", e.getMessage());
            return createOfflineStatus("Bedrock server offline", bedrockPort);
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

    // ============ Java Edition 헬퍼 메소드 ============

    private void writeVarInt(DataOutputStream out, int value) throws IOException {
        while ((value & ~0x7F) != 0) {
            out.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        out.writeByte(value);
    }

    private void writeString(DataOutputStream out, String value) throws IOException {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        writeVarInt(out, bytes.length);
        out.write(bytes);
    }

    private int readVarInt(DataInputStream in) throws IOException {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = in.readByte();
            value |= (currentByte & 0x7F) << position;
            if ((currentByte & 0x80) == 0)
                break;
            position += 7;
            if (position >= 32)
                throw new RuntimeException("VarInt too big");
        }
        return value;
    }

    private String readString(DataInputStream in, int maxLength) throws IOException {
        int length = readVarInt(in);
        if (length > maxLength * 4) {
            throw new IOException("String too long");
        }
        byte[] bytes = new byte[length];
        in.readFully(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private MinecraftDto.ServerStatus parseJavaServerStatus(String json, long latency) {
        try {
            // 간단한 JSON 파싱 (외부 라이브러리 없이)
            String serverName = extractJsonValue(json, "description");
            if (serverName == null) {
                // 복잡한 description 객체인 경우
                int descStart = json.indexOf("\"description\"");
                if (descStart != -1) {
                    int textStart = json.indexOf("\"text\":", descStart);
                    if (textStart != -1) {
                        serverName = extractJsonValueAfter(json, textStart + 7);
                    }
                }
            }
            if (serverName == null)
                serverName = "Minecraft Server";

            String version = "Unknown";
            int versionStart = json.indexOf("\"version\"");
            if (versionStart != -1) {
                int nameStart = json.indexOf("\"name\":", versionStart);
                if (nameStart != -1) {
                    version = extractJsonValueAfter(json, nameStart + 7);
                }
            }

            int maxPlayers = 20;
            int currentPlayers = 0;
            int playersStart = json.indexOf("\"players\"");
            if (playersStart != -1) {
                String maxStr = extractJsonNumberAfter(json, "\"max\":", playersStart);
                String onlineStr = extractJsonNumberAfter(json, "\"online\":", playersStart);
                if (maxStr != null)
                    maxPlayers = Integer.parseInt(maxStr);
                if (onlineStr != null)
                    currentPlayers = Integer.parseInt(onlineStr);
            }

            return new MinecraftDto.ServerStatus(
                    true,
                    serverName + " (Java)",
                    version,
                    currentPlayers,
                    maxPlayers,
                    serverHost,
                    javaPort,
                    latency);

        } catch (Exception e) {
            log.error("Error parsing Java server response: {}", e.getMessage());
            return createOfflineStatus("Parse error", javaPort);
        }
    }

    private String extractJsonValue(String json, String key) {
        String searchKey = "\"" + key + "\":\"";
        int start = json.indexOf(searchKey);
        if (start == -1)
            return null;
        start += searchKey.length();
        int end = json.indexOf("\"", start);
        if (end == -1)
            return null;
        return json.substring(start, end);
    }

    private String extractJsonValueAfter(String json, int startPos) {
        int quoteStart = json.indexOf("\"", startPos);
        if (quoteStart == -1)
            return null;
        int quoteEnd = json.indexOf("\"", quoteStart + 1);
        if (quoteEnd == -1)
            return null;
        return json.substring(quoteStart + 1, quoteEnd);
    }

    private String extractJsonNumberAfter(String json, String key, int searchStart) {
        int keyPos = json.indexOf(key, searchStart);
        if (keyPos == -1)
            return null;
        int start = keyPos + key.length();
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (Character.isDigit(c)) {
                sb.append(c);
            } else if (sb.length() > 0) {
                break;
            }
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    // ============ Bedrock Edition 헬퍼 메소드 ============

    private MinecraftDto.ServerStatus parseBedrockServerStatus(byte[] data, int length, long latency) {
        try {
            if (data[0] != 0x1c) {
                return createOfflineStatus("Invalid response", bedrockPort);
            }

            int stringLength = ((data[33] & 0xFF) << 8) | (data[34] & 0xFF);
            String serverInfo = new String(data, 35, Math.min(stringLength, length - 35), "UTF-8");

            String[] parts = serverInfo.split(";");

            if (parts.length >= 6) {
                return new MinecraftDto.ServerStatus(
                        true,
                        parts[1] + " (Bedrock)",
                        parts.length > 3 ? parts[3] : "Unknown",
                        Integer.parseInt(parts[4]),
                        Integer.parseInt(parts[5]),
                        serverHost,
                        bedrockPort,
                        latency);
            }

            return createOfflineStatus("Parse error", bedrockPort);

        } catch (Exception e) {
            log.error("Error parsing Bedrock server response: {}", e.getMessage());
            return createOfflineStatus("Parse error", bedrockPort);
        }
    }

    private MinecraftDto.ServerStatus createOfflineStatus(String reason, int port) {
        return new MinecraftDto.ServerStatus(
                false,
                reason,
                null,
                0,
                0,
                serverHost,
                port,
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
