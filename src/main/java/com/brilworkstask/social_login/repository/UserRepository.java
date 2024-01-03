package com.brilworkstask.social_login.repository;

import com.brilworkstask.social_login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
