package com.project.team.User;

import com.project.team.DataNotFoundException;
import com.project.team.Reservation.Reservation;
import com.project.team.Reservation.ReservationService;
import com.project.team.test.MailDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class SiteUserController {
    private final SiteUserService siteUserService;
    private final ReservationService reservationService;

    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;


    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "start";
        }
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다.");
            return "start";
        }
        try {
            siteUserService.create(userCreateForm.getLoginId(), userCreateForm.getPassword1(), userCreateForm.getName(), userCreateForm.getEmail(), userCreateForm.getAuthority());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
            return "start";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "start";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/";
    }

    @GetMapping("/userDetail/{loginId}")
    public String userDetail(Model model, @PathVariable("loginId") String loginId) {
        SiteUser siteUser = this.siteUserService.getUser(loginId);
        List<Reservation> userReservation = this.reservationService.getAllByUser(siteUser);
        model.addAttribute("userReservation", userReservation);
        model.addAttribute("siteUser", siteUser);
        return "userDetail";
    }

    @GetMapping("/userModify/{loginId}")
    @PreAuthorize("isAuthenticated()")
    public String userModify(Model model, UserModifyForm userModifyForm, @PathVariable("loginId") String loginId, Principal principal) {
        SiteUser siteUser = this.siteUserService.getUser(loginId);
        if (!siteUser.getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        userModifyForm.setAuthority(siteUser.getAuthority());
        userModifyForm.setPassword(siteUser.getPassword());
        userModifyForm.setName(siteUser.getName());
        userModifyForm.setEmail(siteUser.getEmail());

        return "userModify";
    }

    @PostMapping("/userModify/{loginId}")
    @PreAuthorize("isAuthenticated()")
    public String userModify(Model model, @Valid UserModifyForm userModifyForm, @PathVariable("loginId") String loginId, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "userModify";
        }
        SiteUser siteUser = siteUserService.getUser(loginId);
        if (!siteUser.getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.siteUserService.modifyUser(siteUser, userModifyForm.getName(), userModifyForm.getEmail(), userModifyForm.getPassword(), userModifyForm.getAuthority());
        model.addAttribute("siteUser", siteUser);


        return "userDetail";
    }

    @GetMapping("/findPw")
    public String findPw(){
        return "findPw";
    }

    @PostMapping("/sendEmail")
    public String findPw(String loginId){
        String email = siteUserService.getUser(loginId).getEmail();
        MailDto dto = siteUserService.createMail(email);
        siteUserService.sendPasswordResetEmail(loginId);
        return "redirect:/";
    }

    @GetMapping("/resetPassword/{token}")
    public String showResetPasswordForm(@PathVariable("token") String token, Model model) {
        SiteUser user = siteUserService.getUserByToken(token);
        model.addAttribute("token", token);
        return "resetPasswordForm";
    }

    @PostMapping("/resetPassword/{token}")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
        try {
            // 비밀번호를 재설정
            siteUserService.resetPassword(token, newPassword);
            SiteUser user = siteUserService.getUserByToken(token);
            user.setToken(siteUserService.createToken(user.getLoginId()));

            return "redirect:/"; // 비밀번호 재설정이 성공한 경우 로그인 페이지로 리다이렉트
        } catch (DataNotFoundException e) {
            // 토큰이 유효하지 않은 경우 처리
            return "redirect:/error?message=InvalidToken";
        } catch (Exception e) {
            // 기타 예외 처리
            return "redirect:/error?message=ResetPasswordError";
        }
    }
}

