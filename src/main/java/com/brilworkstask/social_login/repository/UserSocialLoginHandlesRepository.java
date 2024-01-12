package com.brilworkstask.social_login.repository;

import com.brilworkstask.social_login.enums.ProviderEnum;
import com.brilworkstask.social_login.model.UserSocialHandleLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSocialLoginHandlesRepository extends JpaRepository<UserSocialHandleLogin,Long> {
}
