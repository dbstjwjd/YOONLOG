package com.ysblog.sbb.User;

import com.ysblog.sbb.DataNotFoundException;
import com.ysblog.sbb.Post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        user.setImgUrl("/profile/defaultImage.jpeg");
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

    public boolean checkUser(SiteUser user, String email) {
        return user.getEmail().equals(email);
    }

    public SiteUser findUser(String username) {
        Optional<SiteUser> user = this.userRepository.findByUsername(username);
        return user.orElse(null);
    }

    public void updateUserPassword(SiteUser user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
    }

    public List<SiteUser> searchUser(String nickname) {
        return this.userRepository.findByNicknameLike("%" + nickname + "%");
    }

    public void subscribeUser(SiteUser user1, SiteUser user2) {
        user1.getSubscriber().add(user2);
        this.userRepository.save(user1);
    }
}
