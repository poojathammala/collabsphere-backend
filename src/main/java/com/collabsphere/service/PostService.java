package com.collabsphere.service;

import com.collabsphere.dto.PostDto;
import com.collabsphere.entity.Post;
import com.collabsphere.entity.PostType;
import com.collabsphere.entity.User;
import com.collabsphere.exception.ResourceNotFoundException;
import com.collabsphere.exception.UnauthorizedException;
import com.collabsphere.repository.CommentRepository;
import com.collabsphere.repository.PostRepository;
import com.collabsphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CommentRepository commentRepository;

    public PostDto.PostResponse createPost(String email, PostDto.CreatePostRequest request) {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post post = Post.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .domain(request.getDomain())
                .openForCollaboration(request.isOpenForCollaboration())
                .author(author)
                .build();

        if (request.getSkillsRequired() != null)
            post.setSkillsRequired(String.join(",", request.getSkillsRequired()));
        if (request.getSkillsOffered() != null)
            post.setSkillsOffered(String.join(",", request.getSkillsOffered()));

        postRepository.save(post);
        return toPostResponse(post);
    }

    public List<PostDto.PostResponse> getAllPosts(int page, int size) {
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(
                PageRequest.of(page, size));
        return posts.getContent().stream().map(this::toPostResponse).collect(Collectors.toList());
    }

    public PostDto.PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return toPostResponse(post);
    }

    public List<PostDto.PostResponse> getPostsByUser(Long userId) {
        return postRepository.findByAuthorIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toPostResponse).collect(Collectors.toList());
    }

    public List<PostDto.PostResponse> getMyPosts(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return postRepository.findByAuthorIdOrderByCreatedAtDesc(user.getId())
                .stream().map(this::toPostResponse).collect(Collectors.toList());
    }

    public List<PostDto.PostResponse> searchPosts(String query) {
        return postRepository.searchPosts(query)
                .stream().map(this::toPostResponse).collect(Collectors.toList());
    }

    public List<PostDto.PostResponse> getPostsByType(PostType type) {
        return postRepository.findByTypeOrderByCreatedAtDesc(type)
                .stream().map(this::toPostResponse).collect(Collectors.toList());
    }

    public PostDto.PostResponse updatePost(String email, Long postId, PostDto.UpdatePostRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getAuthor().getId().equals(user.getId()))
            throw new UnauthorizedException("You can only edit your own posts");

        if (request.getTitle() != null) post.setTitle(request.getTitle());
        if (request.getDescription() != null) post.setDescription(request.getDescription());
        if (request.getDomain() != null) post.setDomain(request.getDomain());
        if (request.getOpenForCollaboration() != null)
            post.setOpenForCollaboration(request.getOpenForCollaboration());
        if (request.getSkillsRequired() != null)
            post.setSkillsRequired(String.join(",", request.getSkillsRequired()));
        if (request.getSkillsOffered() != null)
            post.setSkillsOffered(String.join(",", request.getSkillsOffered()));

        postRepository.save(post);
        return toPostResponse(post);
    }

    public void deletePost(String email, Long postId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getAuthor().getId().equals(user.getId()))
            throw new UnauthorizedException("You can only delete your own posts");

        postRepository.delete(post);
    }

    public PostDto.PostResponse likePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);
        return toPostResponse(post);
    }

    PostDto.PostResponse toPostResponse(Post post) {
        List<String> skillsReq = post.getSkillsRequired() != null && !post.getSkillsRequired().isBlank()
                ? Arrays.asList(post.getSkillsRequired().split(",")) : Collections.emptyList();
        List<String> skillsOff = post.getSkillsOffered() != null && !post.getSkillsOffered().isBlank()
                ? Arrays.asList(post.getSkillsOffered().split(",")) : Collections.emptyList();
        int commentCount = commentRepository.findByPostIdOrderByCreatedAtAsc(post.getId()).size();

        return PostDto.PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .type(post.getType())
                .skillsRequired(skillsReq)
                .skillsOffered(skillsOff)
                .domain(post.getDomain())
                .openForCollaboration(post.isOpenForCollaboration())
                .likesCount(post.getLikesCount())
                .collaboratorsCount(post.getCollaboratorsCount())
                .commentCount(commentCount)
                .author(AuthService.toUserSummary(post.getAuthor()))
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
