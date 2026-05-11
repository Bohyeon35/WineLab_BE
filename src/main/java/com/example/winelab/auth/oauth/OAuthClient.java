package com.example.winelab.auth.oauth;

import com.example.winelab.domain.member.entity.SocialProvider;

public interface OAuthClient {

    SocialProvider getProvider();

    OAuthUserInfo getUserInfo(String code);
}
