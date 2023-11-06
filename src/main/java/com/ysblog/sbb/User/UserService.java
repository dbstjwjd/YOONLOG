package com.ysblog.sbb.User;

import com.ysblog.sbb.DataNotFoundException;
import com.ysblog.sbb.Post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void createUser(String username, String nickname, String password, String email, String address, LocalDate birthDate) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setNickname(nickname);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setAddress(address);
        user.setBirthDate(birthDate);
        user.setJoinDate(LocalDateTime.now());
        this.userRepository.save(user);
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("user not found");
        }
    }

    public void modifyUser(SiteUser user, String nickname, LocalDate birthDate, String address, String email) {
        user.setNickname(nickname);
        user.setBirthDate(birthDate);
        user.setAddress(address);
        user.setEmail(email);
        this.userRepository.save(user);
    }

    public void socialSignupUser(OidcUser user) {
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername(user.getName());
        siteUser.setEmail(user.getEmail());
        this.userRepository.save(siteUser);
    }
}
