package com.project.team.User;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private String id;

    public OAuthAttributes(String id, Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.id = id;
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public OAuthAttributes() {
    }

    // 해당 로그인인 서비스가 kakao인지 google인지 구분하여, 알맞게 매핑을 해주도록 합니다.
    // 여기서 registrationId는 OAuth2 로그인을 처리한 서비스 명("google","kakao","naver"..)이 되고,
    // userNameAttributeName은 해당 서비스의 map의 키값이 되는 값이됩니다. {google="sub", kakao="id", naver="response"}
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if (registrationId.equals("kakao")) {
            return ofKakao(userNameAttributeName, attributes);
        } else if (registrationId.equals("naver")) {
            return ofNaver(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");  // 카카오로 받은 데이터에서 계정 정보가 담긴 kakao_account 값을 꺼낸다.
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");   // 마찬가지로 profile(nickname, image_url.. 등) 정보가 담긴 값을 꺼낸다.

        System.out.println("kakao" + attributes);
        return new OAuthAttributes(attributes.get("id").toString(),
                attributes,
                userNameAttributeName,
                (String) profile.get("nickname"),
                (String) kakao_account.get("email"),
                (String) profile.get("profile_image_url"));
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");    // 네이버에서 받은 데이터에서 프로필 정보다 담긴 response 값을 꺼낸다.

        System.out.println("naver " + attributes);
        System.out.println("id " + response.get("id"));
        System.out.println("userNameAttributeName " + userNameAttributeName);
        System.out.println("attributes.get(userNameAttributeName) " + attributes.get(userNameAttributeName));


        return new OAuthAttributes((String) response.get("id"),
                (Map<String, Object>) attributes.get(userNameAttributeName),
                "id",
                (String) response.get("name"),
                (String) response.get("email"),
                (String) response.get("profile_image"));
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {

        System.out.println("google" + attributes);
        return new OAuthAttributes(attributes.get("sub").toString(),
                attributes,
                userNameAttributeName,
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                (String) attributes.get("picture"));
    }

    // .. getter/setter 생략

    public SiteUser toEntity() {
        String id = null;
        if (nameAttributeKey.equals("response")) {
            id = attributes.get("id").toString();
        } else id = attributes.get(nameAttributeKey).toString();
        return new SiteUser(id, name, email, picture, LocalDateTime.now());
    }
//    public SiteUser toEntity(){
//        return new SiteUser(id, name, email, picture, LocalDateTime.now());
//    }

}
