package com.example.winelab.auth.controller;

import com.example.winelab.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test", description = "인증 테스트 API")
@RestController
@RequiredArgsConstructor
public class TestController {

    private final AuthService authService;

    @Operation(summary = "JWT 인증 테스트", description = "로그인 후 발급받은 accessToken을 Swagger Authorize에 Bearer JWT로 입력한 뒤 호출합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/test")
    public String test(Principal principal) {
        return authService.test(principal);
    }
}
