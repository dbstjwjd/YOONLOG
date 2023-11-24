package com.project.team.User;

import com.project.team.DataNotFoundException;
import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserRepository;

import com.project.team.test.MailDto;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.maven.model.Site;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public SiteUser create(String loginId, String password, String name, String email, String authority) {
        SiteUser siteUser = new SiteUser();
        siteUser.setLoginId(loginId);
        siteUser.setName(name);
        siteUser.setEmail(email);
        siteUser.setPassword(passwordEncoder.encode(password));
        siteUser.setCreateDate(LocalDateTime.now());
        siteUser.setAuthority(authority);
        siteUserRepository.save(siteUser);
        return siteUser;
    }

    public SiteUser getUser(String loginId) {
        Optional<SiteUser> user = this.siteUserRepository.findByLoginId(loginId);
        if (user.isPresent()) {
            return user.get();
        } else
            throw new DataNotFoundException("user not found");
    }

    public void modifyUser(SiteUser siteUser, String name, String email, String password, String authority) {

        siteUser.setPassword(passwordEncoder.encode(password));
        siteUser.setName(name);
        siteUser.setEmail(email);
        siteUser.setAuthority(authority);

        this.siteUserRepository.save(siteUser);
    }

    public SiteUser getTestUser() {
        Optional<SiteUser> user = this.siteUserRepository.findByLoginId("testUser");
        if (user.isPresent()) {
            return user.get();
        } else return create("testUser", "temp", "익명", null, "손님");
    }






    public MailDto createMail(String email) {
        String str = getAuthNum();
        MailDto dto = new MailDto();
        dto.setAddress(email);
        dto.setTitle("TEAM-PROJECT 인증링크입니다.");
        dto.setMessage("회원님의 인증링크는 " + str + "입니다.");
        return dto;
    }

    public String getAuthNum(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }


    public String createToken(String loginId) {
        String resetToken = getAuthNum(); // 랜덤 토큰 생성
        SiteUser user = getUser(loginId);
        user.setToken(resetToken);
        siteUserRepository.save(user);
        return resetToken;
    }

    public void mailSend(MailDto mailDto) {
        System.out.println("전송 완료!");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        try {
            helper.setTo(mailDto.getAddress());
            helper.setSubject(mailDto.getTitle());

            // HTML 형식의 이메일 내용 설정
            helper.setText(mailDto.getMessage(), true);

            helper.setFrom("teriyaki970326@google.com");
            helper.setReplyTo("teriyaki970326@google.com");

            System.out.println("message: " + message);

            mailSender.send(message);
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }
    }

    public void sendPasswordResetEmail(String loginId) {
        SiteUser user = getUser(loginId);
        String resetToken = createToken(loginId);

        // 비밀번호 재설정 링크를 이메일에 포함시켜 전송
        String resetLink = "http://localhost:8080/user/resetPassword/" + resetToken;
        String emailContent = "<p>비밀번호를 변경하려면 다음 링크를 클릭하세요: <a href='" + resetLink + "'>비밀번호 변경하기</a></p>";
        MailDto dto = new MailDto();
        dto.setAddress(user.getEmail());
        dto.setTitle("[TEAM-PROJECT] 비밀번호 재설정 안내");
        dto.setMessage(emailContent);

        mailSend(dto);
    }

    public void resetPassword(String token, String newPassword) {

        SiteUser user = getUserByToken(token);

        // 새로운 비밀번호를 암호화하여 설정합니다.
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);

        // 변경된 비밀번호를 저장합니다.
        siteUserRepository.save(user);
    }

    public SiteUser getUserByToken(String token) {
        Optional<SiteUser> user = siteUserRepository.findByToken(token);
        if(user.isPresent()){
            return user.get();
        }
        else throw new DataNotFoundException("user not found");
    }

}

