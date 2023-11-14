package com.project.team;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String index() {
        return "start";
    }

    @GetMapping("/map")
    public String map(Model model, String inputAddress) {
        model.addAttribute("inputAddress", inputAddress);
        return "map";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }
}
