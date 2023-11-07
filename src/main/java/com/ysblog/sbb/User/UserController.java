package com.ysblog.sbb.User;

import com.ysblog.sbb.Post.Post;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
        SiteUser user = this.userService.getUser(principal.getName());
        model.addAttribute("user", user);
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
    public String modify(Principal principal, @PathVariable("username") String username, @Valid UserModifyForm userModifyForm, BindingResult bindingResult, Model model, @RequestParam(value = "file") MultipartFile file) {
        SiteUser user = this.userService.getUser(principal.getName());
        model.addAttribute("user", user);
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();
                String ext = fileName.substring(file.getOriginalFilename().lastIndexOf("."));
                String path = "src/main/resources/static/profile/" + user.getUsername() + ext;
                File temp = new File(path);
                user.setImgUrl("/profile/" + user.getUsername() + ext);
                file.transferTo(new File(temp.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else user.setImgUrl("/profile/defaultImage.jpeg");
        if (bindingResult.hasErrors()) {
            return "user_modify";
        }
        this.userService.modifyUser(user, userModifyForm.getNickname(), userModifyForm.getBirthDate(), userModifyForm.getAddress(), userModifyForm.getEmail());
        return String.format("redirect:/user/info/%s", username);
    }

    @GetMapping("/check")
    public String userCheck() {
        return "userCheck_form";
    }

    @PostMapping("/user/check")
    public ResponseEntity<String> checkUser(@RequestParam String username, @RequestParam String email) {
        SiteUser user = this.userService.findUser(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아이디가 존재하지 않습니다.");
        }

        // 유저의 이메일과 입력받은 이메일 비교
        if (user.getEmail().equals(email)) {
            return ResponseEntity.ok("아이디와 이메일이 일치합니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일이 일치하지 않습니다.");
        }
    }
}



