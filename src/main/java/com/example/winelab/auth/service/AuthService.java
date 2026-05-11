package com.example.winelab.auth.service;

import com.example.winelab.auth.dto.TokenDto;
import com.example.winelab.auth.jwt.TokenProvider;
import com.example.winelab.auth.oauth.OAuthClient;
import com.example.winelab.auth.oauth.OAuthUserInfo;
import com.example.winelab.domain.member.entity.Role;
import com.example.winelab.domain.member.entity.SocialProvider;
import com.example.winelab.domain.member.entity.User;
import com.example.winelab.domain.member.repository.UserRepository;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final Map<SocialProvider, OAuthClient> oAuthClients;

    public AuthService(UserRepository userRepository, TokenProvider tokenProvider, List<OAuthClient> oAuthClients) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.oAuthClients = oAuthClients.stream()
                .collect(Collectors.toMap(OAuthClient::getProvider, Function.identity()));
    }

    @Transactional
    public TokenDto loginOrSignUp(SocialProvider provider, String code) {
        OAuthUserInfo userInfo = getOAuthClient(provider).getUserInfo(code);

        if (Boolean.FALSE.equals(userInfo.getVerifiedEmail())) {
            throw new RuntimeException("Email is not verified.");
        }

        User user = userRepository.findBySocialProviderAndSocialId(provider, userInfo.getProviderId())
                .or(() -> userRepository.findByEmail(userInfo.getEmail()))
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(userInfo.getEmail())
                        .name(userInfo.getName())
                        .pictureUrl(userInfo.getPictureUrl())
                        .socialProvider(provider)
                        .socialId(userInfo.getProviderId())
                        .role(Role.ROLE_USER)
                        .build()));

        return tokenProvider.createToken(user);
    }

    private OAuthClient getOAuthClient(SocialProvider provider) {
        OAuthClient oAuthClient = oAuthClients.get(provider);
        if (oAuthClient == null) {
            throw new RuntimeException("Unsupported social provider: " + provider);
        }
        return oAuthClient;
    }

    @Transactional(readOnly = true)
    public String test(Principal principal) {
        Long id = Long.parseLong(principal.getName());

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found."))
                .getEmail();
    }
}
