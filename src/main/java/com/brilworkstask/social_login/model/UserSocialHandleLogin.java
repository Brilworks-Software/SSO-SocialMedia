package com.brilworkstask.social_login.model;

import com.brilworkstask.social_login.enums.ProviderEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "social_handle_login")
public class UserSocialHandleLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false,insertable = false,updatable = false)
    User user;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    ProviderEnum provider;

    public UserSocialHandleLogin(Long userId, ProviderEnum provider) {
        this.userId = userId;
        this.provider = provider;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Long getUserId() {
        return userId;
    }

    public ProviderEnum getProvider() {
        return provider;
    }
}
