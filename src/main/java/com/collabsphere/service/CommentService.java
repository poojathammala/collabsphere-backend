package com.collabsphere.service;

import com.collabsphere.dto.CommentDto;
import com.collabsphere.entity.Comment;
import com.collabsphere.entity.Post;
import com.collabsphere.entity.User;
import com.collabsphere.exception.ResourceNotFoundException;
import com.collabsphere.repository.CommentRepository;
import com.collabsphere.repository.PostRepository;
import com.collabsphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired private CommentRepository commentRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;

    public CommentDto.CommentResponse addComment(String email, Long postId, CommentDto.CreateCommentRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(user)
                .post(post)
                .build();

        commentRepository.save(comment);
        return toResponse(comment);
    }

    public List<CommentDto.CommentResponse> getCommentsByPost(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public void deleteComment(String email, Long commentId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        if (!comment.getAuthor().getId().equals(user.getId()))
            throw new RuntimeException("Unauthorized");
        commentRepository.delete(comment);
    }

    private CommentDto.CommentResponse toResponse(Comment comment) {
        return CommentDto.CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(AuthService.toUserSummary(comment.getAuthor()))
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
