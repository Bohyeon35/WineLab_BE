package com.example.winelab.auth.controller;

import com.example.winelab.auth.service.AuthService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final AuthService authService;

    @GetMapping("/test")
    public String test(Principal principal) {
        return authService.test(principal);
    }
}
