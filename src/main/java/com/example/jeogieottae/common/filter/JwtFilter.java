package com.example.jeogieottae.common.filter;

import com.example.jeogieottae.common.dto.AuthUser;
import com.example.jeogieottae.common.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private static final Set<String> EXCLUDED_URIS = Set.of(
            "/auth/signup",
            "/auth/signin",
            "/coupons",
            "/accommodations",
            "/infra",
            "/oauth2",
            "/login/oauth2"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUDED_URIS.contains(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = extractToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtUtil.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰");
            return;
        }

        Long userId = jwtUtil.extractUserId(token);
        String userEmail = jwtUtil.extractUserEmail(token);
        String username = jwtUtil.extractUsername(token);

        AuthUser authUser = AuthUser.of(userId, userEmail, username);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(authUser, null, List.of())
        );

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(JwtUtil.BEARER_PREFIX)) {
            return header.substring(JwtUtil.BEARER_PREFIX.length());
        }

        if (request.getCookies() == null) return null;

        for (var cookie : request.getCookies()) {
            if ("accessToken".equals(cookie.getName())) {
                String value = cookie.getValue();
                if (value.startsWith(JwtUtil.BEARER_PREFIX)) {
                    return value.substring(JwtUtil.BEARER_PREFIX.length());
                }
                return value;
            }
        }

        return null;
    }
}