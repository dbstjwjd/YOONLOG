package com.project.team.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModifyForm {


    @Size(min = 3, max = 25)
    @NotEmpty(message = "수정할 이름을 입력하세요.")
    private String name;

    @NotEmpty(message = "수정할 이메일을 입력하세요.")
    @Email
    private String email;

    @NotEmpty(message = "수정할 패스워드를 입력하세요.")
    private String password;

    private String authority;
}
