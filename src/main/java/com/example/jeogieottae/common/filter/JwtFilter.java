package com.example.jeogieottae.common.filter;

import com.example.jeogieottae.common.dto.AuthUser;
import com.example.jeogieottae.common.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.equals("/auth/signup") || uri.equals("/auth/signin");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT가 필요합니다.");
            return;
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "JWT가 존재하지 않습니다.");
            return;
        }

        String jwt = authorizationHeader.substring(7);

        if (!jwtUtil.validateToken(jwt)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        Long userId = jwtUtil.extractUserId(jwt);
        String userEmail = jwtUtil.extractUserEmail(jwt);
        String username = jwtUtil.extractUsername(jwt);

        AuthUser authUser = AuthUser.of(userId, userEmail, username);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, null, List.of()));

        filterChain.doFilter(request, response);
    }
}