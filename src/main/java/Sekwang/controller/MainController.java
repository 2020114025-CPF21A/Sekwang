package Sekwang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MainController {
    @GetMapping("/")
    public String mainpage() {
        return "main.html";
    }
}
