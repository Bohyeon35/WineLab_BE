package com.example.winelab.auth.oauth;

import com.example.winelab.auth.dto.KakaoUserInfoDto;
import com.example.winelab.auth.dto.KakaoUserInfoDto.KakaoAccount;
import com.example.winelab.auth.dto.KakaoUserInfoDto.Profile;
import com.example.winelab.auth.dto.OAuthTokenResponseDto;
import com.example.winelab.domain.member.entity.SocialProvider;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuthClient implements OAuthClient {

    private final Gson gson = new Gson();

    @Value("${oauth.kakao.token-url}")
    private String kakaoTokenUrl;

    @Value("${oauth.kakao.user-info-url}")
    private String kakaoUserInfoUrl;

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth.kakao.client-secret:}")
    private String kakaoClientSecret;

    @Value("${oauth.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.KAKAO;
    }

    @Override
    public OAuthUserInfo getUserInfo(String code) {
        String accessToken = getAccessToken(code);
        KakaoUserInfoDto userInfoDto = requestUserInfo(accessToken);
        KakaoAccount kakaoAccount = userInfoDto.getKakaoAccount();
        Profile profile = kakaoAccount == null ? null : kakaoAccount.getProfile();

        if (kakaoAccount == null || !StringUtils.hasText(kakaoAccount.getEmail())) {
            throw new RuntimeException("Kakao email is required.");
        }

        if (profile == null || !StringUtils.hasText(profile.getNickname())
                || !StringUtils.hasText(profile.getProfileImageUrl())) {
            throw new RuntimeException("Kakao profile is required.");
        }

        return OAuthUserInfo.builder()
                .provider(getProvider())
                .providerId(String.valueOf(userInfoDto.getId()))
                .email(kakaoAccount.getEmail())
                .verifiedEmail(kakaoAccount.getEmailVerified())
                .name(profile.getNickname())
                .pictureUrl(profile.getProfileImageUrl())
                .build();
    }

    private String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("grant_type", "authorization_code");

        if (StringUtils.hasText(kakaoClientSecret)) {
            params.add("client_secret", kakaoClientSecret);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(kakaoTokenUrl, requestEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return gson.fromJson(responseEntity.getBody(), OAuthTokenResponseDto.class).getAccessToken();
        }

        throw new RuntimeException("Kakao access token request failed.");
    }

    private KakaoUserInfoDto requestUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                kakaoUserInfoUrl,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return gson.fromJson(responseEntity.getBody(), KakaoUserInfoDto.class);
        }

        throw new RuntimeException("Kakao user info request failed.");
    }
}
