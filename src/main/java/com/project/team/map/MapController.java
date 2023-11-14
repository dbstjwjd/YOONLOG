package com.project.team.map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map")
public class MapController {
    @GetMapping("/view")
    public String search(Model model, String inputAddress) {
        model.addAttribute("inputAddress", inputAddress);
        return "map";
    }
}
