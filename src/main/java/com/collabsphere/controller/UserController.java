package com.collabsphere.controller;

import com.collabsphere.dto.UserDto;
import com.collabsphere.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto.UserProfile> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getMyProfile(userDetails.getUsername()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto.UserProfile> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserDto.UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateProfile(userDetails.getUsername(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto.UserProfile> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto.UserSummary>> searchUsers(@RequestParam String query) {
        return ResponseEntity.ok(userService.searchUsers(query));
    }

    @GetMapping("/search/skill")
    public ResponseEntity<List<UserDto.UserSummary>> searchBySkill(@RequestParam String skill) {
        return ResponseEntity.ok(userService.searchBySkill(skill));
    }

    @GetMapping
    public ResponseEntity<List<UserDto.UserSummary>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
