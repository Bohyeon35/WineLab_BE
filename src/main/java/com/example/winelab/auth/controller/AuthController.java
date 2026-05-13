package com.example.winelab.auth.controller;

import com.example.winelab.auth.dto.LoginResponseDto;
import com.example.winelab.auth.service.OAuthAuthorizationService;
import com.example.winelab.auth.service.AuthService;
import com.example.winelab.domain.member.entity.SocialProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Social login APIs")
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OAuthAuthorizationService oAuthAuthorizationService;

    @Operation(summary = "Google 로그인 URL 조회", description = "Swagger에서 실행한 뒤 응답 URL을 브라우저에서 열면 Google 로그인 화면으로 이동합니다.")
    @GetMapping("/oauth/google/authorization-url")
    public String googleAuthorizationUrl() {
        return oAuthAuthorizationService.getAuthorizationUrl(SocialProvider.GOOGLE);
    }

    @Operation(summary = "Kakao 로그인 URL 조회", description = "Swagger에서 실행한 뒤 응답 URL을 브라우저에서 열면 Kakao 로그인 화면으로 이동합니다.")
    @GetMapping("/oauth/kakao/authorization-url")
    public String kakaoAuthorizationUrl() {
        return oAuthAuthorizationService.getAuthorizationUrl(SocialProvider.KAKAO);
    }

    @Operation(summary = "Google 로그인 시작", description = "브라우저에서 직접 호출하면 Google 로그인 화면으로 redirect됩니다.")
    @GetMapping("/login/google")
    public ResponseEntity<Void> googleLogin() {
        return redirectTo(oAuthAuthorizationService.getAuthorizationUrl(SocialProvider.GOOGLE));
    }

    @Operation(summary = "Kakao 로그인 시작", description = "브라우저에서 직접 호출하면 Kakao 로그인 화면으로 redirect됩니다.")
    @GetMapping("/login/kakao")
    public ResponseEntity<Void> kakaoLogin() {
        return redirectTo(oAuthAuthorizationService.getAuthorizationUrl(SocialProvider.KAKAO));
    }

    @Operation(summary = "Google 로그인 콜백", description = "Google 로그인 성공 후 전달받은 code로 WineLab JWT를 발급합니다.")
    @GetMapping("/callback/google")
    public LoginResponseDto googleCallback(@RequestParam("code") String code) {
        return authService.loginOrSignUp(SocialProvider.GOOGLE, code);
    }

    @Operation(summary = "Kakao 로그인 콜백", description = "Kakao 로그인 성공 후 전달받은 code로 WineLab JWT를 발급합니다.")
    @GetMapping("/callback/kakao")
    public LoginResponseDto kakaoCallback(@RequestParam("code") String code) {
        return authService.loginOrSignUp(SocialProvider.KAKAO, code);
    }

    private ResponseEntity<Void> redirectTo(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(url));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
