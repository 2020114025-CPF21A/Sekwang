package Sekwang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    @GetMapping({"/", "/app"})
    public String appEntry() {
        return "forward:/app/index.html";
    }

    // /app 이하 모든 경로: 정적 파일(.포함)이면 그대로, 아니면 SPA index.html
    @GetMapping("/app/{*path}")
    public String appRoutes(@PathVariable String path) {
        if (path.contains(".")) {
            return "forward:/app/" + path;
        }
        return "forward:/app/index.html";
    }
}
