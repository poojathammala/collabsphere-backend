package com.collabsphere.service;

import com.collabsphere.dto.UserDto;
import com.collabsphere.entity.User;
import com.collabsphere.exception.ResourceNotFoundException;
import com.collabsphere.repository.PostRepository;
import com.collabsphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;

    public UserDto.UserProfile getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toUserProfile(user);
    }

    public UserDto.UserProfile getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toUserProfile(user);
    }

    public UserDto.UserProfile updateProfile(String email, UserDto.UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getCollege() != null) user.setCollege(request.getCollege());
        if (request.getDepartment() != null) user.setDepartment(request.getDepartment());
        if (request.getYear() != null) user.setYear(request.getYear());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
        if (request.getLinkedinUrl() != null) user.setLinkedinUrl(request.getLinkedinUrl());
        if (request.getGithubUrl() != null) user.setGithubUrl(request.getGithubUrl());
        if (request.getPortfolioUrl() != null) user.setPortfolioUrl(request.getPortfolioUrl());
        if (request.getSkills() != null) {
            user.setSkills(String.join(",", request.getSkills()));
        }

        userRepository.save(user);
        return toUserProfile(user);
    }

    public List<UserDto.UserSummary> searchUsers(String query) {
        return userRepository.searchUsers(query).stream()
                .map(AuthService::toUserSummary)
                .collect(Collectors.toList());
    }

    public List<UserDto.UserSummary> searchBySkill(String skill) {
        return userRepository.searchBySkill(skill).stream()
                .map(AuthService::toUserSummary)
                .collect(Collectors.toList());
    }

    public List<UserDto.UserSummary> getAllUsers() {
        return userRepository.findAll().stream()
                .map(AuthService::toUserSummary)
                .collect(Collectors.toList());
    }

    private UserDto.UserProfile toUserProfile(User user) {
        List<String> skills = user.getSkills() != null && !user.getSkills().isBlank()
                ? Arrays.asList(user.getSkills().split(","))
                : Collections.emptyList();
        int postCount = postRepository.findByAuthorIdOrderByCreatedAtDesc(user.getId()).size();
        return UserDto.UserProfile.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .college(user.getCollege())
                .department(user.getDepartment())
                .year(user.getYear())
                .bio(user.getBio())
                .avatarUrl(user.getAvatarUrl())
                .linkedinUrl(user.getLinkedinUrl())
                .githubUrl(user.getGithubUrl())
                .portfolioUrl(user.getPortfolioUrl())
                .skills(skills)
                .postCount(postCount)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
