package com.example.winelab.preference.repository;

import com.example.winelab.preference.entity.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    Optional<Preference> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}