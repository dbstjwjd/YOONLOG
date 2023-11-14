package com.project.team.User;

import com.project.team.DataNotFoundException;
import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String loginId, String password, String username, String email){
        SiteUser siteUser = new SiteUser();
        siteUser.setLoginId(loginId);
        siteUser.setName(username);
        siteUser.setEmail(email);
        siteUser.setPassword(passwordEncoder.encode(password));
        siteUserRepository.save(siteUser);
        return siteUser;
    }

    public SiteUser getUser(String LoginId) {
        Optional<SiteUser> user = this.siteUserRepository.findByLoginId(LoginId);
        if (user.isPresent()) {
            return user.get();
        } else
            throw new DataNotFoundException("user not found");
    }
}
