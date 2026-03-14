package com.collabsphere.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

public class UserDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSummary {
        private Long id;
        private String email;
        private String fullName;
        private String username;
        private String college;
        private String department;
        private String year;
        private String bio;
        private String avatarUrl;
        private List<String> skills;
        private LocalDateTime createdAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserProfile {
        private Long id;
        private String email;
        private String fullName;
        private String username;
        private String college;
        private String department;
        private String year;
        private String bio;
        private String avatarUrl;
        private String linkedinUrl;
        private String githubUrl;
        private String portfolioUrl;
        private List<String> skills;
        private int postCount;
        private LocalDateTime createdAt;
    }

    @Data
    public static class UpdateProfileRequest {
        private String fullName;
        private String username;
        private String college;
        private String department;
        private String year;
        private String bio;
        private String avatarUrl;
        private String linkedinUrl;
        private String githubUrl;
        private String portfolioUrl;
        private List<String> skills;
    }
}
