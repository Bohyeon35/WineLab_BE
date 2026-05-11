package com.example.winelab.auth.oauth;

import com.example.winelab.domain.member.entity.SocialProvider;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthUserInfo {

    private SocialProvider provider;
    private String providerId;
    private String email;
    private Boolean verifiedEmail;
    private String name;
    private String pictureUrl;
}
