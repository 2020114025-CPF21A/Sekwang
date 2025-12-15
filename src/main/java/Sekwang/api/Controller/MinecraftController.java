package Sekwang.api.Controller;

import Sekwang.Service.MinecraftService;
import Sekwang.api.DTO.MinecraftDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/minecraft")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
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
}
