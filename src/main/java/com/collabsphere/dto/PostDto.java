package com.collabsphere.dto;

import com.collabsphere.entity.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

public class PostDto {

    @Data
    public static class CreatePostRequest {
        @NotBlank(message = "Title is required")
        private String title;

        @NotBlank(message = "Description is required")
        private String description;

        @NotNull(message = "Post type is required")
        private PostType type;

        private List<String> skillsRequired;
        private List<String> skillsOffered;
        private String domain;
        private boolean openForCollaboration = true;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostResponse {
        private Long id;
        private String title;
        private String description;
        private PostType type;
        private List<String> skillsRequired;
        private List<String> skillsOffered;
        private String domain;
        private boolean openForCollaboration;
        private int likesCount;
        private int collaboratorsCount;
        private int commentCount;
        private UserDto.UserSummary author;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    public static class UpdatePostRequest {
        private String title;
        private String description;
        private List<String> skillsRequired;
        private List<String> skillsOffered;
        private String domain;
        private Boolean openForCollaboration;
    }
}
