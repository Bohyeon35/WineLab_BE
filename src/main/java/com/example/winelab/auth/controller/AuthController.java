package com.example.winelab.auth.controller;

import com.example.winelab.auth.dto.LoginResponseDto;
import com.example.winelab.auth.service.AuthService;
import com.example.winelab.domain.member.entity.SocialProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/callback/google")
    public LoginResponseDto googleCallback(@RequestParam("code") String code) {
        return authService.loginOrSignUp(SocialProvider.GOOGLE, code);
    }

    @GetMapping("/callback/kakao")
    public LoginResponseDto kakaoCallback(@RequestParam("code") String code) {
        return authService.loginOrSignUp(SocialProvider.KAKAO, code);
    }
}
