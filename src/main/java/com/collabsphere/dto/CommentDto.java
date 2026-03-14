package com.collabsphere.dto;

import com.collabsphere.entity.RequestStatus;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

public class CommentDto {

    @Data
    public static class CreateCommentRequest {
        private String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentResponse {
        private Long id;
        private String content;
        private UserDto.UserSummary author;
        private Long postId;
        private LocalDateTime createdAt;
    }
}
