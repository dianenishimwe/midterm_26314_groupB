package com.bus.busmanagement.service;

import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bus.busmanagement.dto.UserDTO;
import com.bus.busmanagement.model.User;
import com.bus.busmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(UserDTO userDTO) {
        User user = User.builder()
            .username(userDTO.getUsername())
            .password(passwordEncoder.encode(userDTO.getPassword()))
            .email(userDTO.getEmail())
            .fullName(userDTO.getName())
            .role(User.UserRole.valueOf(userDTO.getRole()))
            .build();
        return userRepository.save(Objects.requireNonNull(user));
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public Page<User> getAllUsersPaginated(int page, int size, String sortBy, boolean ascending) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsersSorted(String sortBy) {
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(@NonNull Long id) {
        return userRepository.findById(Objects.requireNonNull(id))
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public User updateUser(@NonNull Long id, UserDTO userDTO) {
        User user = getUserById(Objects.requireNonNull(id));
        user.setFullName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        if (userDTO.getRole() != null && !userDTO.getRole().isEmpty()) {
            user.setRole(User.UserRole.valueOf(userDTO.getRole()));
        }
        return userRepository.save(Objects.requireNonNull(user));
    }

    public void deleteUser(@NonNull Long id) {
        User user = getUserById(Objects.requireNonNull(id));
        userRepository.delete(Objects.requireNonNull(user));
    }
}