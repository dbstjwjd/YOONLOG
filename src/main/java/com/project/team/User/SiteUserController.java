package com.project.team.User;

import com.project.team.Reservation.Reservation;
import com.project.team.Reservation.ReservationService;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.maven.model.Site;
import org.hibernate.engine.jdbc.mutation.spi.BindingGroup;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
    public String userModify(Model model,@Valid UserModifyForm userModifyForm, @PathVariable("loginId") String loginId, Principal principal, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "userModify";
        }
        SiteUser siteUser = siteUserService.getUser(loginId);
        if(!siteUser.getLoginId().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.siteUserService.modifyUser(siteUser,userModifyForm.getName(), userModifyForm.getEmail(), userModifyForm.getPassword(), userModifyForm.getAuthority());
        model.addAttribute("siteUser", siteUser);


        return "userDetail";
    }



















}
