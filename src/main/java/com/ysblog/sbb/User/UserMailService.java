package com.ysblog.sbb.User;

import com.ysblog.sbb.User.SiteUser;
import com.ysblog.sbb.User.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Component
public class UserMailService {
    @Value("${spring.mail.username}")
    private String from;

    private final UserRepository userRepository;

    private final JavaMailSender javaMailSender;

    private final PasswordEncoder passwordEncoder;

    private final TemplateEngine templateEngine;

    public String createPassword() {
        String password = "";
        for (int i = 0; i < 12; i++) {
            password += (char) ((Math.random() * 26) + 97);
        }
        return password;
    }

    public void updatePassword(SiteUser user, String pw) {
        user.setPassword(passwordEncoder.encode(pw));
        this.userRepository.save(user);
    }

    public void sendEmail(SiteUser user) {
        String userPW = createPassword();
        updatePassword(user, userPW);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        Context context = new Context();
        context.setVariable("userNickname", user.getNickname());
        context.setVariable("temporaryPassword", userPW);
        String emailContent = templateEngine.process("mail_view", context);
        try {
            helper.setTo(user.getEmail());
            helper.setFrom(from);
            helper.setSubject("[윤로그] 임시 비밀번호 발급 안내");
            helper.setText(emailContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
