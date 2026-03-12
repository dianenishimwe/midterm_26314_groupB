package com.bus.busmanagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bus.busmanagement.dto.UserDTO;
import com.bus.busmanagement.model.User;
import com.bus.busmanagement.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        User savedUser = userService.registerUser(userDTO);
        UserDTO response = UserDTO.builder()
            .id(savedUser.getId())
            .name(savedUser.getFullName())
            .email(savedUser.getEmail())
            .username(savedUser.getUsername())
            .role(savedUser.getRole().name())
            .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers().stream()
            .map(u -> UserDTO.builder()
                .id(u.getId())
                .name(u.getFullName())
                .email(u.getEmail())
                .username(u.getUsername())
                .role(u.getRole().name())
                .build())
            .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            UserDTO response = UserDTO.builder()
                .id(user.getId())
                .name(user.getFullName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            UserDTO response = UserDTO.builder()
                .id(user.getId())
                .name(user.getFullName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            UserDTO response = UserDTO.builder()
                .id(user.getId())
                .name(user.getFullName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Map<String, Boolean>> checkUserExistsByEmail(@PathVariable String email) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", userService.existsByEmail(email));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Map<String, Boolean>> checkUserExistsByUsername(@PathVariable String username) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", userService.existsByUsername(username));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<UserDTO>> getUsersPaginated(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "fullName") String sortBy,
        @RequestParam(defaultValue = "true") boolean ascending
    ) {
        Page<UserDTO> userPage = userService.getAllUsersPaginated(page, size, sortBy, ascending)
            .map(u -> UserDTO.builder()
                .id(u.getId())
                .name(u.getFullName())
                .email(u.getEmail())
                .username(u.getUsername())
                .role(u.getRole().name())
                .build());
        return ResponseEntity.ok(userPage);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<UserDTO>> getUsersSorted(
        @RequestParam(defaultValue = "fullName") String sortBy
    ) {
        List<UserDTO> users = userService.getAllUsersSorted(sortBy).stream()
            .map(u -> UserDTO.builder()
                .id(u.getId())
                .name(u.getFullName())
                .email(u.getEmail())
                .username(u.getUsername())
                .role(u.getRole().name())
                .build())
            .toList();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            User updatedUser = userService.updateUser(id, userDTO);
            UserDTO response = UserDTO.builder()
                .id(updatedUser.getId())
                .name(updatedUser.getFullName())
                .email(updatedUser.getEmail())
                .username(updatedUser.getUsername())
                .role(updatedUser.getRole().name())
                .build();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}