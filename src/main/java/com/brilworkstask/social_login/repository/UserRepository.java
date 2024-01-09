package com.brilworkstask.social_login.repository;

import com.brilworkstask.social_login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.referenceId FROM User u")
    List<String> findReferenceIds();

}
