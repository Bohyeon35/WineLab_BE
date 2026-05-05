package com.example.winelab.preference.service;

import com.example.winelab.preference.dto.request.PreferenceRequest;
import com.example.winelab.preference.dto.response.PreferenceResponse;
import com.example.winelab.preference.entity.Preference;
import com.example.winelab.preference.repository.PreferenceRepository;
import com.example.winelab.global.exception.CustomException;
import com.example.winelab.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;

    @Transactional
    public PreferenceResponse savePreference(PreferenceRequest request) {

        if (preferenceRepository.existsByUserId(request.getUserId())) {
            throw new CustomException(ErrorCode.PREFERENCE_ALREADY_EXISTS);
        }

        Preference preference = Preference.builder()
                .userId(request.getUserId())
                .tannin(roundToHalf(request.getTannin()))
                .acidity(roundToHalf(request.getAcidity()))
                .body(roundToHalf(request.getBody()))
                .sweetness(roundToHalf(request.getSweetness()))
                .build();

        return new PreferenceResponse(preferenceRepository.save(preference));
    }

    @Transactional(readOnly = true)
    public PreferenceResponse getPreference(Long userId) {
        Preference preference = preferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.PREFERENCE_NOT_FOUND));

        return new PreferenceResponse(preference);
    }

    private Double roundToHalf(Double value) {
        return Math.round(value * 2) / 2.0;
    }
}