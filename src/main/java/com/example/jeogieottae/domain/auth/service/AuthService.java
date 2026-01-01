package com.example.jeogieottae.domain.auth.service;

import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.util.JwtUtil;
import com.example.jeogieottae.common.util.PasswordEncoder;
import com.example.jeogieottae.domain.auth.dto.request.SignUpRequest;
import com.example.jeogieottae.domain.auth.dto.response.SignUpResponse;
import com.example.jeogieottae.domain.user.entity.User;
import com.example.jeogieottae.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.jeogieottae.common.exception.ErrorCode.USER_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignUpResponse signUp(SignUpRequest request) {
        String userEmail = request.getEmail();
        String username = request.getUsername();

        if (userRepository.existsByEmail(userEmail)) {
            throw new CustomException(USER_ALREADY_EXISTS);
        }

        User user = User.from(userEmail, username, passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String token = jwtUtil.generateToken(username, userEmail, user.getId());

        return new SignUpResponse(token);
    }
}
