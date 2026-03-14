package com.collabsphere.service;

import com.collabsphere.dto.AuthDto;
import com.collabsphere.dto.UserDto;
import com.collabsphere.entity.User;
import com.collabsphere.exception.BadRequestException;
import com.collabsphere.repository.UserRepository;
import com.collabsphere.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtils jwtUtils;

    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }
        if (request.getUsername() != null && userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .college(request.getCollege())
                .department(request.getDepartment())
                .year(request.getYear())
                .username(request.getUsername() != null ? request.getUsername()
                        : request.getEmail().split("@")[0])
                .build();

        userRepository.save(user);

        String token = jwtUtils.generateTokenFromEmail(user.getEmail());
        return new AuthDto.AuthResponse(token, toUserSummary(user));
    }

    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return new AuthDto.AuthResponse(token, toUserSummary(user));
    }

    public static UserDto.UserSummary toUserSummary(User user) {
        List<String> skills = user.getSkills() != null && !user.getSkills().isBlank()
                ? Arrays.asList(user.getSkills().split(","))
                : Collections.emptyList();
        return UserDto.UserSummary.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .college(user.getCollege())
                .department(user.getDepartment())
                .year(user.getYear())
                .bio(user.getBio())
                .avatarUrl(user.getAvatarUrl())
                .skills(skills)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
