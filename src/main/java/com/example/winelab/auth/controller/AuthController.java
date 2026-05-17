package com.example.winelab.auth.controller;

import com.example.winelab.auth.dto.LoginResponseDto;
import com.example.winelab.auth.service.AuthService;
import com.example.winelab.auth.service.OAuthAuthorizationService;
import com.example.winelab.domain.member.entity.SocialProvider;
import io.swagger.v3.oas.annotations.Hidden;
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

@Tag(name = "Auth", description = "소셜 로그인 API")
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OAuthAuthorizationService oAuthAuthorizationService;

    @Hidden
    @GetMapping("/oauth/google/authorization-url")
    public String googleAuthorizationUrl() {
        return oAuthAuthorizationService.getAuthorizationUrl(SocialProvider.GOOGLE);
    }

    @Hidden
    @GetMapping("/oauth/kakao/authorization-url")
    public String kakaoAuthorizationUrl() {
        return oAuthAuthorizationService.getAuthorizationUrl(SocialProvider.KAKAO);
    }

    @Operation(
            summary = "Google 로그인",
            description = "Swagger 테스트는 [Google 로그인 바로가기](/api/login/google)를 클릭해서 진행하세요. Execute를 누르면 302 redirect 응답만 확인됩니다."
    )
    @GetMapping("/login/google")
    public ResponseEntity<Void> googleLogin() {
        return redirectTo(oAuthAuthorizationService.getAuthorizationUrl(SocialProvider.GOOGLE));
    }

    @Operation(
            summary = "Kakao 로그인",
            description = "Swagger 테스트는 [Kakao 로그인 바로가기](/api/login/kakao)를 클릭해서 진행하세요. Execute를 누르면 302 redirect 응답만 확인됩니다."
    )
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
