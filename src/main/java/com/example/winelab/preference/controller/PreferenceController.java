package com.example.winelab.preference.controller;

import com.example.winelab.preference.dto.request.PreferenceRequest;
import com.example.winelab.preference.dto.response.PreferenceResponse;
import com.example.winelab.preference.service.PreferenceService;
import com.example.winelab.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/preferences")
public class PreferenceController {

    private final PreferenceService preferenceService;

    @PostMapping
    public ResponseEntity<ApiResponse<PreferenceResponse>> savePreference(
            @RequestBody @Valid PreferenceRequest request) {
        return ResponseEntity.ok(ApiResponse.success("취향 설정이 완료되었습니다.", preferenceService.savePreference(request)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<PreferenceResponse>> getPreference(
            @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(preferenceService.getPreference(userId)));
    }
}