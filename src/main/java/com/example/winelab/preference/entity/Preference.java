package com.example.winelab.preference.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "preference")
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Double tannin;

    private Double acidity;

    private Double body;

    private Double sweetness;

    @Builder
    public Preference(Long userId, Double tannin, Double acidity, Double body, Double sweetness) {
        this.userId = userId;
        this.tannin = tannin;
        this.acidity = acidity;
        this.body = body;
        this.sweetness = sweetness;
    }

    public void update(Double tannin, Double acidity, Double body, Double sweetness) {
        this.tannin = tannin;
        this.acidity = acidity;
        this.body = body;
        this.sweetness = sweetness;
    }
}