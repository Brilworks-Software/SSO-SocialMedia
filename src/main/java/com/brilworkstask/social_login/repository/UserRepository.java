package com.brilworkstask.social_login.repository;

import com.brilworkstask.social_login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM User e WHERE e.email = :email")
    boolean existByEmailId(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM User e WHERE e.socialId = :socialId")
    boolean existBySocialId(@Param("socialId") String socialId);

    boolean existsByEmail(String email);
    User findByEmail(String email);
    boolean existsBySocialId(String socialId);
    User findBySocialId(String socialId);
}
