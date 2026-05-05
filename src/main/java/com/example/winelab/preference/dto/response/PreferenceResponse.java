package com.example.winelab.preference.dto.response;

import com.example.winelab.preference.entity.Preference;
import lombok.Getter;

@Getter
public class PreferenceResponse {

    private Long id;
    private Long userId;
    private Double tannin;
    private Double acidity;
    private Double body;
    private Double sweetness;

    public PreferenceResponse(Preference preference) {
        this.id = preference.getId();
        this.userId = preference.getUserId();
        this.tannin = preference.getTannin();
        this.acidity = preference.getAcidity();
        this.body = preference.getBody();
        this.sweetness = preference.getSweetness();
    }
}