package Sekwang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

public class SpaForwardController {
    // /app로 시작하고 .이 없는 모든 경로를 index.html로 전달
    @RequestMapping({"/app/{path:[^\\.]*}", "/app/**/{path:[^\\.]*}"})
    public String forwardApp() { return "forward:/app/index.html"; }
}
