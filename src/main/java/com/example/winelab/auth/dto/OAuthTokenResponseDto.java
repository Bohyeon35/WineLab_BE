package com.example.winelab.auth.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OAuthTokenResponseDto {

    @SerializedName("access_token")
    private String accessToken;
}
