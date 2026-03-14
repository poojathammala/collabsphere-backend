package com.collabsphere.service;

import com.collabsphere.dto.CollaborationRequestDto;
import com.collabsphere.entity.*;
import com.collabsphere.exception.BadRequestException;
import com.collabsphere.exception.ResourceNotFoundException;
import com.collabsphere.repository.CollaborationRequestRepository;
import com.collabsphere.repository.PostRepository;
import com.collabsphere.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollaborationRequestService {

    @Autowired private CollaborationRequestRepository requestRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PostService postService;

    public CollaborationRequestDto.RequestResponse sendRequest(String email, CollaborationRequestDto.CreateRequest req) {
        User requester = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post post = postRepository.findById(req.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (post.getAuthor().getId().equals(requester.getId()))
            throw new BadRequestException("Cannot request to collaborate on your own post");

        if (requestRepository.existsByRequesterIdAndPostId(requester.getId(), post.getId()))
            throw new BadRequestException("You have already sent a request for this post");

        CollaborationRequest request = CollaborationRequest.builder()
                .requester(requester)
                .post(post)
                .message(req.getMessage())
                .status(RequestStatus.PENDING)
                .build();

        requestRepository.save(request);
        return toResponse(request);
    }

    public List<CollaborationRequestDto.RequestResponse> getIncomingRequests(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return requestRepository.findByPostAuthorId(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<CollaborationRequestDto.RequestResponse> getMyRequests(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return requestRepository.findByRequesterId(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CollaborationRequestDto.RequestResponse updateStatus(String email, Long requestId,
                                                                 CollaborationRequestDto.UpdateStatusRequest req) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CollaborationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (!request.getPost().getAuthor().getId().equals(user.getId()))
            throw new BadRequestException("Only the post owner can update this request");

        request.setStatus(req.getStatus());

        if (req.getStatus() == RequestStatus.ACCEPTED) {
            Post post = request.getPost();
            post.setCollaboratorsCount(post.getCollaboratorsCount() + 1);
            postRepository.save(post);
        }

        requestRepository.save(request);
        return toResponse(request);
    }

    private CollaborationRequestDto.RequestResponse toResponse(CollaborationRequest req) {
        return CollaborationRequestDto.RequestResponse.builder()
                .id(req.getId())
                .requester(AuthService.toUserSummary(req.getRequester()))
                .post(postService.toPostResponse(req.getPost()))
                .message(req.getMessage())
                .status(req.getStatus())
                .createdAt(req.getCreatedAt())
                .build();
    }
}
