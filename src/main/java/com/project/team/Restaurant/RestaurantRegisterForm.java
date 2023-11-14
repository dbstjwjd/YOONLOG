package com.project.team.Restaurant;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantRegisterForm {

    @Size(max = 20)
    @NotEmpty(message = "가게 이름을 입력해주세요.")
    private String name;

    @NotEmpty(message = "가게 주소를 입력해주세요.")
    private String address;

    @NotEmpty(message = "가게 전화번호를 입력해주세요.")
    private String number;
}
