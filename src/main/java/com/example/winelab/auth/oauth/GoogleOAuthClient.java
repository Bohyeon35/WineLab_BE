package com.example.winelab.auth.oauth;

import com.example.winelab.auth.dto.GoogleUserInfoDto;
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
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleOAuthClient implements OAuthClient {

    private final Gson gson = new Gson();

    @Value("${oauth.google.token-url}")
    private String googleTokenUrl;

    @Value("${oauth.google.user-info-url}")
    private String googleUserInfoUrl;

    @Value("${oauth.google.client-id}")
    private String googleClientId;

    @Value("${oauth.google.client-secret}")
    private String googleClientSecret;

    @Value("${oauth.google.redirect-uri}")
    private String googleRedirectUri;

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.GOOGLE;
    }

    @Override
    public OAuthUserInfo getUserInfo(String code) {
        String accessToken = getAccessToken(code);
        GoogleUserInfoDto userInfoDto = requestUserInfo(accessToken);

        return OAuthUserInfo.builder()
                .provider(getProvider())
                .providerId(userInfoDto.getId())
                .email(userInfoDto.getEmail())
                .verifiedEmail(userInfoDto.getVerifiedEmail())
                .name(userInfoDto.getName())
                .pictureUrl(userInfoDto.getPictureUrl())
                .build();
    }

    private String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(googleTokenUrl, requestEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return gson.fromJson(responseEntity.getBody(), OAuthTokenResponseDto.class).getAccessToken();
        }

        throw new RuntimeException("Google access token request failed.");
    }

    private GoogleUserInfoDto requestUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                googleUserInfoUrl,
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return gson.fromJson(responseEntity.getBody(), GoogleUserInfoDto.class);
        }

        throw new RuntimeException("Google user info request failed.");
    }
}
