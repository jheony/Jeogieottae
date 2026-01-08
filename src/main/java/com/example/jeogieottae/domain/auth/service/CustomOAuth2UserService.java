package com.example.jeogieottae.domain.auth.service;

import com.example.jeogieottae.domain.user.entity.User;
import com.example.jeogieottae.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService <OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Long kakaoId = ((Number) attributes.get("id")).longValue();

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        if (kakaoAccount == null) {
            throw new OAuth2AuthenticationException("kakao_account missing");
        }

        String email = (String) kakaoAccount.get("email");

        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String nickname = profile != null
                ? (String) profile.get("nickname")
                : "kakao-user";

        //provider + providerId 기준 조회
        User user = userRepository
                .findByProviderAndProviderId(provider, kakaoId.toString())
                .orElseGet(() -> userRepository.save(
                        User.createKakao(
                                email,
                                nickname,
                                kakaoId.toString()
                        )
                ));

        return new DefaultOAuth2User(
                List.of(() -> "ROLE_USER"),
                attributes,
                "id"
        );
    }
}
