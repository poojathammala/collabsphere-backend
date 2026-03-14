package com.collabsphere.controller;

import com.collabsphere.dto.PostDto;
import com.collabsphere.entity.PostType;
import com.collabsphere.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired private PostService postService;

    @PostMapping
    public ResponseEntity<PostDto.PostResponse> createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PostDto.CreatePostRequest request) {
        return ResponseEntity.ok(postService.createPost(userDetails.getUsername(), request));
    }

    @GetMapping
    public ResponseEntity<List<PostDto.PostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(postService.getAllPosts(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto.PostResponse> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDto.PostResponse>> getPostsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    @GetMapping("/me")
    public ResponseEntity<List<PostDto.PostResponse>> getMyPosts(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(postService.getMyPosts(userDetails.getUsername()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostDto.PostResponse>> searchPosts(@RequestParam String query) {
        return ResponseEntity.ok(postService.searchPosts(query));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<PostDto.PostResponse>> getPostsByType(@PathVariable PostType type) {
        return ResponseEntity.ok(postService.getPostsByType(type));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto.PostResponse> updatePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody PostDto.UpdatePostRequest request) {
        return ResponseEntity.ok(postService.updatePost(userDetails.getUsername(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        postService.deletePost(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<PostDto.PostResponse> likePost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.likePost(id));
    }
}
