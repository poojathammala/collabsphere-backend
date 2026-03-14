package com.collabsphere.controller;

import com.collabsphere.dto.CommentDto;
import com.collabsphere.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired private CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto.CommentResponse> addComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody CommentDto.CreateCommentRequest request) {
        return ResponseEntity.ok(commentService.addComment(userDetails.getUsername(), postId, request));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto.CommentResponse>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long commentId) {
        commentService.deleteComment(userDetails.getUsername(), commentId);
        return ResponseEntity.noContent().build();
    }
}
