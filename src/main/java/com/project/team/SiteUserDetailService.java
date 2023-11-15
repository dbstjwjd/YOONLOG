package com.project.team;

import com.project.team.User.SiteUser;
import com.project.team.User.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SiteUserDetailService implements UserDetailsService {
    private final SiteUserRepository siteUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SiteUser> siteUserOptional = siteUserRepository.findByLoginId(username);
        if (siteUserOptional.isEmpty()) {
            throw new UsernameNotFoundException(username + "라는 아이디가 존재하지 않습니다.");
        }
        SiteUser siteUser = siteUserOptional.get();
        String loginId = siteUser.getLoginId();
        String password = siteUser.getPassword();

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("user"));
        return new User(loginId, password, authorities);
    }
}
