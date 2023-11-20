package com.project.team.SMS;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class SMSController {

    private final SMSService smsService;

    public String createRandomNumber() {
        Random rand = new Random();
        String randomNum = "";
        for (int i = 0; i < 4; i++) {
            String random = Integer.toString(rand.nextInt(10));
            randomNum += random;
        }

        return randomNum;
    }

    @GetMapping("/send")
    public String send(@RequestParam String phoneNumber, HttpSession session) {
        String verKey = createRandomNumber();
        smsService.sendMessage(phoneNumber, verKey);

        // 세션에 verKey 저장
        session.setAttribute("verKey", verKey);

        return "redirect:/";
    }


    @GetMapping("/sendVerKey")
    public ResponseEntity<Map<String, String>> getVerificationKey(HttpSession session) {
        // 세션에서 verKey 가져오기
        String verKey = (String) session.getAttribute("verKey");

        Map<String, String> response = new HashMap<>();
        response.put("verKey", verKey);
        return ResponseEntity.ok(response);
    }

}
