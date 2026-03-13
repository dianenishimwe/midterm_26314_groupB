package com.bus.busmanagement.repository;

import com.bus.busmanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByRole(User.UserRole role);

    @Override
    @NonNull
    Page<User> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    List<User> findAll(@NonNull Sort sort);
}