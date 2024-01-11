package com.brilworkstask.social_login.model;

import com.brilworkstask.social_login.enums.ProviderEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.internal.build.AllowPrintStacktrace;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "social_id")
    private String socialId;

    public User(String firstName, String lastName, String email, String socialId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.socialId = socialId;

    }
}
