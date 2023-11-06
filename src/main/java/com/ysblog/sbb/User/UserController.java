package com.ysblog.sbb.User;

import com.ysblog.sbb.Post.Post;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserForm userForm, Model model, Principal principal) {
        if (principal != null) {
            SiteUser user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserForm userForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "signup_form";
        if (!userForm.getPassword1().equals(userForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "signup_form";
        }
        try {
            this.userService.createUser(userForm.getUsername(), userForm.getNickname(), userForm.getPassword1(), userForm.getEmail(), userForm.getAddress(), userForm.getBirthDate());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.rejectValue("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.rejectValue("signupFailed", e.getMessage());
            return "signup_form";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login_form";
    }

    @GetMapping("/info/{username}")
    public String info(Model model, Principal principal, @PathVariable("username") String username) {
        SiteUser siteUser = this.userService.getUser(principal.getName());
        model.addAttribute("user", siteUser);
        return "user_info";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{username}")
    public String modify(@PathVariable("username") String username, Model model, Principal principal, UserModifyForm userModifyForm) {
        SiteUser user = this.userService.getUser(principal.getName());
        model.addAttribute("user", user);
        userModifyForm.setNickname(user.getNickname());
        userModifyForm.setBirthDate(user.getBirthDate());
        userModifyForm.setAddress(user.getAddress());
        userModifyForm.setEmail(user.getEmail());
        return "user_modify";
    }

    @PostMapping("/modify/{username}")
    @PreAuthorize("isAuthenticated()")
    public String modify(Principal principal, @PathVariable("username") String username, @Valid UserModifyForm userModifyForm, BindingResult bindingResult, Model model) {
        SiteUser user = this.userService.getUser(principal.getName());
        model.addAttribute("user", user);
        if (bindingResult.hasErrors()) {
            return "user_modify";
        }
        this.userService.modifyUser(user, userModifyForm.getNickname(), userModifyForm.getBirthDate(), userModifyForm.getAddress(), userModifyForm.getEmail());
        return String.format("redirect:/user/info/%s", username);
    }
}


