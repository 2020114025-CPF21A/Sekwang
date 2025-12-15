package Sekwang.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
