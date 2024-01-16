package com.brilworkstask.social_login.repository;

import com.brilworkstask.social_login.enums.ProviderEnum;
import com.brilworkstask.social_login.model.UserProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProviderRepository extends JpaRepository<UserProvider, Long> {
    boolean existsByUserIdAndProvider(Long userId, ProviderEnum provider);
}
