package com.ysblog.sbb.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class UserModifyForm {

    @NotEmpty(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate birthDate;

    @NotEmpty(message = "주소를 입력해주세요")
    private String address;

    @Email
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;

    private MultipartFile profileImage;

}
