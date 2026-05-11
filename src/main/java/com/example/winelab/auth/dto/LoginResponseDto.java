package com.example.winelab.auth.dto;

import com.example.winelab.domain.member.entity.SocialProvider;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {

    private String accessToken;
    private String tokenType;
    private Long userId;
    private String email;
    private String name;
    private String pictureUrl;
    private SocialProvider socialProvider;
}
