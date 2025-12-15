package Sekwang.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class MinecraftDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServerStatus {
        private boolean online;
        private String serverName;
        private String version;
        private int currentPlayers;
        private int maxPlayers;
        private String serverAddress;
        private int port;
        private long latency; // ms
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayerList {
        private boolean online;
        private int count;
        private List<String> players;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayerInfo {
        private String name;
        private String xuid;
    }

    // 플레이어 로그 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayerLogResponse {
        private Long id;
        private String playerName;
        private String eventType; // JOIN or LEAVE
        private LocalDateTime eventTime;
        private Long sessionDurationMinutes;
    }

    // 플레이어 통계 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayerStats {
        private String playerName;
        private long totalPlayTimeMinutes;
        private long totalSessions;
        private LocalDateTime lastSeen;
    }

    // 랭킹 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayTimeRanking {
        private int rank;
        private String playerName;
        private long totalPlayTimeMinutes;
        private String formattedPlayTime; // "5시간 30분" 형식
    }

    // 오늘의 통계 DTO
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailyStats {
        private long uniquePlayers;
        private long totalSessions;
        private List<PlayerLogResponse> recentLogs;
    }
}
