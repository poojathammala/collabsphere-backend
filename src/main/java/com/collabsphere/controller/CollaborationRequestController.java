package com.collabsphere.controller;

import com.collabsphere.dto.CollaborationRequestDto;
import com.collabsphere.service.CollaborationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collaborations")
public class CollaborationRequestController {

    @Autowired private CollaborationRequestService service;

    @PostMapping("/request")
    public ResponseEntity<CollaborationRequestDto.RequestResponse> sendRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CollaborationRequestDto.CreateRequest request) {
        return ResponseEntity.ok(service.sendRequest(userDetails.getUsername(), request));
    }

    @GetMapping("/incoming")
    public ResponseEntity<List<CollaborationRequestDto.RequestResponse>> getIncoming(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.getIncomingRequests(userDetails.getUsername()));
    }

    @GetMapping("/sent")
    public ResponseEntity<List<CollaborationRequestDto.RequestResponse>> getSent(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.getMyRequests(userDetails.getUsername()));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CollaborationRequestDto.RequestResponse> updateStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody CollaborationRequestDto.UpdateStatusRequest request) {
        return ResponseEntity.ok(service.updateStatus(userDetails.getUsername(), id, request));
    }
}
