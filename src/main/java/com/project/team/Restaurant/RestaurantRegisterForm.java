package com.project.team.Restaurant;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class RestaurantRegisterForm {

    @NotEmpty(message = "가게 이름을 입력해주세요.")
    private String name;

    @NotEmpty(message = "대표 번호를 입력해주세요.")
    private String number;

    @NotEmpty(message = "메인 메뉴를 입력해주세요.")
    private String main;

    private String introduce;

    @NotEmpty(message = "가게 주소를 입력해주세요.")
    private String address;

    @NotEmpty(message = "영업 시작시간을 입력해주세요.")
    private LocalTime startTime;

    @NotEmpty(message = "가게 영업 마감시간을 입력해주세요.")
    private LocalTime endTime;

    private List<String> facilities;
}
