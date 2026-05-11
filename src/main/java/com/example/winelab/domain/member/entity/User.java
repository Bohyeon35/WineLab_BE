package com.example.winelab.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "USER")
public class User {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_NAME", nullable = false)
    private String name;

    @Column(name = "USER_EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "USER_PICTURE", nullable = false)
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_PROVIDER")
    private SocialProvider socialProvider;

    @Column(name = "USER_PROVIDER_ID")
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_ROLE", nullable = false)
    private Role role;

    public void updateSocialInfo(
            String name,
            String pictureUrl,
            SocialProvider socialProvider,
            String socialId
    ) {
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.socialProvider = socialProvider;
        this.socialId = socialId;
    }
}
