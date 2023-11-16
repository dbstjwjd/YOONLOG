package com.project.team;

import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserService;
import com.project.team.User.UserCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final SiteUserService siteUserService;

    @GetMapping("/")
    public String index(UserCreateForm userCreateForm) {
        return "start";
    }

    @GetMapping("/main")
    public String mainPage(Model model, Principal principal) {
        SiteUser user = this.siteUserService.getUser(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("inputAddress", "aroundMe");
        return "main";
    }

    @GetMapping("/regTest")
    public String regTest() {
        return "registerForm";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
