package com.brilworkstask.social_login.repository;

import com.brilworkstask.social_login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    User findByEmail(String email);
    boolean existsBySocialId(String socialId);
    User findBySocialId(String socialId);
}
