package com.ysblog.sbb;

import com.ysblog.sbb.User.SiteUser;
import com.ysblog.sbb.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String root() {
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String main(Model model, Principal principal) {
        if (principal != null) {
            SiteUser user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        return "main";
    }
}
