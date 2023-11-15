package com.project.team.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "로그인 아이디를 입력하세요.")
    private String loginId;

    @Size(min = 3, max = 20)
    @NotEmpty(message = "이름을 입력하세요.")
    private String name;

    @NotEmpty(message = "로그인 비밀번호를 입력하세요.")
    private String password1;

    @NotEmpty(message = "로그인 비밀번호를 확인해주세요.")
    private String password2;

    @NotEmpty(message = "이메일을 입력하세요.")
    @Email
    private String email;
}
