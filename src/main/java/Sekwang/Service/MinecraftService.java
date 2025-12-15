package Sekwang.Service;

import Sekwang.api.DTO.MinecraftDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class MinecraftService {

    @Value("${minecraft.server.host:localhost}")
    private String serverHost;

    @Value("${minecraft.server.port:19132}")
    private int serverPort;

    private static final byte[] UNCONNECTED_PING = new byte[]{
            0x01, // ID
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // Time
            0x00, (byte) 0xff, (byte) 0xff, 0x00, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0xfe,
            (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, (byte) 0xfd, 0x12, 0x34, 0x56, 0x78, // Magic
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00  // Client GUID
    };

    /**
     * 베드락 서버 상태 조회 (RakNet 프로토콜)
     */
    public MinecraftDto.ServerStatus getServerStatus() {
        long startTime = System.currentTimeMillis();
        
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(3000); // 3초 타임아웃
            
            InetAddress address = InetAddress.getByName(serverHost);
            
            // Unconnected Ping 전송
            byte[] pingPacket = createPingPacket();
            DatagramPacket sendPacket = new DatagramPacket(pingPacket, pingPacket.length, address, serverPort);
            socket.send(sendPacket);
            
            // 응답 수신
            byte[] receiveData = new byte[4096];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            
            long latency = System.currentTimeMillis() - startTime;
            
            // 응답 파싱
            return parseServerStatus(receivePacket.getData(), receivePacket.getLength(), latency);
            
        } catch (SocketTimeoutException e) {
            log.warn("Minecraft server timeout: {}", e.getMessage());
            return createOfflineStatus("Server timeout");
        } catch (Exception e) {
            log.error("Error querying Minecraft server: {}", e.getMessage());
            return createOfflineStatus("Connection error");
        }
    }

    /**
     * 현재 접속 중인 플레이어 목록
     * 베드락 서버는 기본적으로 플레이어 목록을 제공하지 않음
     * 로그 파일 파싱 또는 RCON 확장이 필요
     */
    public MinecraftDto.PlayerList getPlayers() {
        MinecraftDto.ServerStatus status = getServerStatus();
        
        if (!status.isOnline()) {
            return new MinecraftDto.PlayerList(false, 0, new ArrayList<>());
        }
        
        // 베드락 서버는 기본적으로 플레이어 이름 목록을 제공하지 않음
        // 플레이어 수만 반환
        return new MinecraftDto.PlayerList(
                true,
                status.getCurrentPlayers(),
                new ArrayList<>() // 플레이어 이름은 로그 파싱 필요
        );
    }

    private byte[] createPingPacket() {
        byte[] packet = Arrays.copyOf(UNCONNECTED_PING, UNCONNECTED_PING.length);
        // 현재 시간 설정
        long time = System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.wrap(packet, 1, 8);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(time);
        // 랜덤 GUID 설정
        new Random().nextBytes(Arrays.copyOfRange(packet, 25, 33));
        return packet;
    }

    private MinecraftDto.ServerStatus parseServerStatus(byte[] data, int length, long latency) {
        try {
            if (data[0] != 0x1c) { // Unconnected Pong ID
                return createOfflineStatus("Invalid response");
            }
            
            // 서버 정보 문자열 추출 (오프셋 35부터)
            int stringLength = ((data[33] & 0xFF) << 8) | (data[34] & 0xFF);
            String serverInfo = new String(data, 35, Math.min(stringLength, length - 35), "UTF-8");
            
            // 세미콜론으로 구분된 정보 파싱
            // 형식: Edition;MOTD;ProtocolVersion;VersionName;Players;MaxPlayers;ServerUniqueId;WorldName;Gamemode;...
            String[] parts = serverInfo.split(";");
            
            if (parts.length >= 6) {
                return new MinecraftDto.ServerStatus(
                        true,
                        parts[1], // MOTD (서버 이름)
                        parts.length > 3 ? parts[3] : "Unknown", // 버전
                        Integer.parseInt(parts[4]), // 현재 플레이어
                        Integer.parseInt(parts[5]), // 최대 플레이어
                        serverHost,
                        serverPort,
                        latency
                );
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
                -1
        );
    }
}
