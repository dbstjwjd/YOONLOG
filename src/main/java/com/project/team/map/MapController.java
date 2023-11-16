package com.project.team.map;

import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

    private final SiteUserService siteUserService;

    @GetMapping("/view")
    public String search(Model model, String inputAddress, Principal principal) {
        if (!principal.getName().isEmpty()) {
            SiteUser user = this.siteUserService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        model.addAttribute("inputAddress", inputAddress);
        return "main";
    }
}
