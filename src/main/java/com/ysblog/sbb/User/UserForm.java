package com.ysblog.sbb.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserForm {

    @Size(min = 3, max = 25)
    @NotEmpty(message = "아이디를 입력해주세요.")
    private String username;

    @NotEmpty(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인을 입력해주세요")
    private String password2;

    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate birthDate;

    @NotEmpty(message = "주소를 입력해주세요.")
    private String address;

    @Email
    @NotEmpty(message = "이메일을 입력해주세요")
    private String email;
}
