package com.example.winelab.domain.member.repository;

import com.example.winelab.domain.member.entity.SocialProvider;
import com.example.winelab.domain.member.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);
}
