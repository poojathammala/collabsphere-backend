package com.collabsphere.dto;

import com.collabsphere.entity.RequestStatus;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

public class CollaborationRequestDto {

    @Data
    public static class CreateRequest {
        private Long postId;
        private String message;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestResponse {
        private Long id;
        private UserDto.UserSummary requester;
        private PostDto.PostResponse post;
        private String message;
        private RequestStatus status;
        private LocalDateTime createdAt;
    }

    @Data
    public static class UpdateStatusRequest {
        private RequestStatus status;
    }
}
