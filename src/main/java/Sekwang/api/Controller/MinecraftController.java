package Sekwang.api.Controller;

import Sekwang.Service.MinecraftService;
import Sekwang.api.DTO.MinecraftDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/minecraft")
@RequiredArgsConstructor
public class MinecraftController {

    private final MinecraftService minecraftService;

    /**
     * 마인크래프트 서버 상태 조회
     * GET /api/minecraft/status
     */
    @GetMapping("/status")
    public MinecraftDto.ServerStatus getServerStatus() {
        return minecraftService.getServerStatus();
    }

    /**
     * 현재 접속 중인 플레이어 목록
     * GET /api/minecraft/players
     */
    @GetMapping("/players")
    public MinecraftDto.PlayerList getPlayers() {
        return minecraftService.getPlayers();
    }

    // ============ 로그 API ============

    /**
     * 최근 접속 로그 조회
     * GET /api/minecraft/logs
     */
    @GetMapping("/logs")
    public List<MinecraftDto.PlayerLogResponse> getRecentLogs() {
        return minecraftService.getRecentLogs();
    }

    /**
     * 특정 플레이어의 로그 조회
     * GET /api/minecraft/logs/{playerName}
     */
    @GetMapping("/logs/{playerName}")
    public List<MinecraftDto.PlayerLogResponse> getPlayerLogs(@PathVariable String playerName) {
        return minecraftService.getPlayerLogs(playerName);
    }

    /**
     * 오늘의 통계
     * GET /api/minecraft/stats/daily
     */
    @GetMapping("/stats/daily")
    public MinecraftDto.DailyStats getDailyStats() {
        return minecraftService.getDailyStats();
    }

    /**
     * 플레이 시간 랭킹
     * GET /api/minecraft/stats/ranking
     */
    @GetMapping("/stats/ranking")
    public List<MinecraftDto.PlayTimeRanking> getPlayTimeRanking() {
        return minecraftService.getPlayTimeRanking();
    }

    // ============ 이벤트 기록 API (GET으로 우회) ============

    /**
     * 플레이어 입장 기록 (GET 방식 - 로그 모니터용)
     * GET /api/minecraft/log/join?player=PlayerName
     */
    @GetMapping("/log/join")
    public Map<String, String> logPlayerJoin(@RequestParam String player) {
        if (player == null || player.isBlank()) {
            return Map.of("error", "player is required");
        }
        minecraftService.recordPlayerJoin(player);
        return Map.of("status", "ok", "event", "join", "player", player);
    }

    /**
     * 플레이어 퇴장 기록 (GET 방식 - 로그 모니터용)
     * GET /api/minecraft/log/leave?player=PlayerName
     */
    @GetMapping("/log/leave")
    public Map<String, String> logPlayerLeave(@RequestParam String player) {
        if (player == null || player.isBlank()) {
            return Map.of("error", "player is required");
        }
        minecraftService.recordPlayerLeave(player);
        return Map.of("status", "ok", "event", "leave", "player", player);
    }

    // ============ 이벤트 기록 API (POST 방식 - 레거시) ============

    /**
     * 플레이어 입장 기록
     * POST /api/minecraft/events/join
     */
    @PostMapping("/events/join")
    public ResponseEntity<Map<String, String>> recordPlayerJoin(@RequestBody Map<String, String> request) {
        String playerName = request.get("playerName");
        if (playerName == null || playerName.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "playerName is required"));
        }
        minecraftService.recordPlayerJoin(playerName);
        return ResponseEntity.ok(Map.of("message", "Player join recorded", "player", playerName));
    }

    /**
     * 플레이어 퇴장 기록
     * POST /api/minecraft/events/leave
     */
    @PostMapping("/events/leave")
    public ResponseEntity<Map<String, String>> recordPlayerLeave(@RequestBody Map<String, String> request) {
        String playerName = request.get("playerName");
        if (playerName == null || playerName.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "playerName is required"));
        }
        minecraftService.recordPlayerLeave(playerName);
        return ResponseEntity.ok(Map.of("message", "Player leave recorded", "player", playerName));
    }
}
