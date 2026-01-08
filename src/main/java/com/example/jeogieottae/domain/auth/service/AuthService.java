package com.example.jeogieottae.domain.auth.service;

import com.example.jeogieottae.common.exception.CustomException;
import com.example.jeogieottae.common.exception.ErrorCode;
import com.example.jeogieottae.common.utils.JwtUtil;
import com.example.jeogieottae.common.utils.PasswordEncoder;
import com.example.jeogieottae.domain.auth.dto.request.SignInRequest;
import com.example.jeogieottae.domain.auth.dto.request.SignUpRequest;
import com.example.jeogieottae.domain.auth.dto.response.SignInResponse;
import com.example.jeogieottae.domain.auth.dto.response.SignUpResponse;
import com.example.jeogieottae.domain.user.entity.User;
import com.example.jeogieottae.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = User.create(userEmail, username, passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String token = jwtUtil.generateToken(username, userEmail, user.getId());

        return new SignUpResponse(token);
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest request) {

        String userEmail = request.getEmail();
        String rawPassword = request.getPassword();

        User foundUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (foundUser.isDeleted()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(rawPassword, foundUser.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        String token = jwtUtil.generateToken(foundUser.getUsername(), foundUser.getEmail(), foundUser.getId());

        return new SignInResponse(token);
    }
}
