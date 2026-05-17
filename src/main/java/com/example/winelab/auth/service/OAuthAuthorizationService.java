package com.example.winelab.auth.service;

import com.example.winelab.domain.member.entity.SocialProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OAuthAuthorizationService {

    @Value("${oauth.google.authorization-url}")
    private String googleAuthorizationUrl;

    @Value("${oauth.google.client-id}")
    private String googleClientId;

    @Value("${oauth.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${oauth.kakao.authorization-url}")
    private String kakaoAuthorizationUrl;

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    public String getAuthorizationUrl(SocialProvider provider) {
        if (provider == SocialProvider.GOOGLE) {
            return buildGoogleAuthorizationUrl();
        }

        if (provider == SocialProvider.KAKAO) {
            return buildKakaoAuthorizationUrl();
        }

        throw new RuntimeException("Unsupported social provider: " + provider);
    }

    private String buildGoogleAuthorizationUrl() {
        return UriComponentsBuilder.fromUriString(googleAuthorizationUrl)
                .queryParam("client_id", googleClientId)
                .queryParam("redirect_uri", googleRedirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "email profile")
                .build()
                .encode()
                .toUriString();
    }

    private String buildKakaoAuthorizationUrl() {
        return UriComponentsBuilder.fromUriString(kakaoAuthorizationUrl)
                .queryParam("client_id", kakaoClientId)
                .queryParam("redirect_uri", kakaoRedirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "account_email profile_nickname profile_image")
                .build()
                .encode()
                .toUriString();
    }
}
