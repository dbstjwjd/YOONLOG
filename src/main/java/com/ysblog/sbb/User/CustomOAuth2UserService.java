package com.ysblog.sbb.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = getUserEmail(oAuth2User, userRequest.getClientRegistration().getRegistrationId());
        String name = getUserName(oAuth2User, userRequest.getClientRegistration().getRegistrationId());
        String username = getUserId(oAuth2User, userRequest.getClientRegistration().getRegistrationId());

        Optional<SiteUser> userOptional = this.userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            SiteUser user = userOptional.get();
            user.setNickname(name);
            this.userRepository.save(user);
        } else {
            SiteUser newUser = new SiteUser();
            newUser.setEmail(email);
            newUser.setUsername(username);
            newUser.setNickname(name);
            newUser.setJoinDate(LocalDateTime.now());
            this.userRepository.save(newUser);
        }
        if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            TestOauthUser tuser = new TestOauthUser();
            String naming = oAuth2User.getName();
            int idx1 = naming.indexOf("=") + 1;
            int idx2 = naming.indexOf(",");
            String getName = naming.substring(idx1, idx2);
            tuser.setName(getName);
            return tuser;
        }

        return oAuth2User;
    }

    private String getUserEmail(OAuth2User oAuth2User, String registrationId) {
        if ("google".equals(registrationId)) {
            return oAuth2User.getAttribute("email");
        } else if ("naver".equals(registrationId)) {
            Map<String, Object> naverAttributes = oAuth2User.getAttribute("response");
            if (naverAttributes != null) {
                return (String) naverAttributes.get("email");
            }
        } else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAttributes = oAuth2User.getAttribute("kakao_account");
            if (kakaoAttributes != null) {
                return (String) kakaoAttributes.get("email");
            }
        }
        return null;
    }

    private String getUserName(OAuth2User oAuth2User, String registrationId) {
        if ("google".equals(registrationId)) {
            return oAuth2User.getAttribute("name");
        } else if ("naver".equals(registrationId)) {
            Map<String, Object> naverAttributes = oAuth2User.getAttribute("response");
            if (naverAttributes != null) {
                return (String) naverAttributes.get("name");
            }
        } else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAttributes = oAuth2User.getAttribute("properties");
            if (kakaoAttributes != null) {
                return (String) kakaoAttributes.get("nickname");
            }
        }
        return null;
    }

    private String getUserId(OAuth2User oAuth2User, String registrationId) {
        if ("google".equals(registrationId)) {
            return oAuth2User.getAttribute("sub");
        } else if ("naver".equals(registrationId)) {
            Map<String, Object> naverAttributes = oAuth2User.getAttribute("response");
            if (naverAttributes != null) {
                return (String) naverAttributes.get("id");
            }
        } else if ("kakao".equals(registrationId)) {
            Object kakaoId = oAuth2User.getAttribute("id");
            if (kakaoId instanceof Long) {
                return Long.toString((Long) kakaoId);
            } else if (kakaoId instanceof String) {
                return (String) kakaoId;
            }
        }
        return null;
    }
}
