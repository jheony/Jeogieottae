package com.example.jeogieottae.common.util;

import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.exception.ErrorCode;
import com.example.jeogieottae.domain.user.entity.User;
import com.example.jeogieottae.domain.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Long kakaoId = ((Number) oAuth2User.getAttribute("id")).longValue();
        User user = userRepository
                .findByProviderAndProviderId("kakao", kakaoId.toString())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String jwt = jwtUtil.generateToken(
                user.getUsername(),
                user.getEmail(),
                user.getId()
        );
        Cookie cookie = new Cookie("accessToken", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int) (JwtUtil.TOKEN_TIME / 1000));

        response.addCookie(cookie);
        response.sendRedirect("/auth/me");
    }
}
