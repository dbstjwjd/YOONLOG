package com.project.team.map;

import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

    private final SiteUserService siteUserService;

    @GetMapping("/view")
    public String search(Model model,
                         @RequestParam(value = "inputAddress", defaultValue = "aroundMe") String inputAddress,
                         Principal principal) {
        if (principal != null) {
            SiteUser user = this.siteUserService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        model.addAttribute("inputAddress", inputAddress);
        return "main";
    }
}
