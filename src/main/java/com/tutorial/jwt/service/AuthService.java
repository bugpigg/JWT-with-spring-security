package com.tutorial.jwt.service;

import com.tutorial.jwt.controller.dto.MemberLoginRequestDto;
import com.tutorial.jwt.controller.dto.MemberSignUpRequestDto;
import com.tutorial.jwt.controller.dto.MemberSignUpResponseDto;
import com.tutorial.jwt.controller.dto.TokenReissueRequestDto;
import com.tutorial.jwt.controller.dto.TokenResponseDto;
import com.tutorial.jwt.domain.Member;
import com.tutorial.jwt.domain.RefreshToken;
import com.tutorial.jwt.repository.MemberRepository;
import com.tutorial.jwt.repository.RefreshTokenRepository;
import com.tutorial.jwt.token.TokenProvider;
import com.tutorial.jwt.token.TokenSet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    @Transactional
    public MemberSignUpResponseDto signUp(MemberSignUpRequestDto requestDto) {
        String userName = requestDto.getUsername();
        if (memberRepository.existsByUserName(userName)) {
            throw new RuntimeException("Duplicated Email exist");
        }

        Member member = requestDto.toMember(passwordEncoder);
        memberRepository.save(member);
        return MemberSignUpResponseDto.of(member);
    }

    @Transactional
    public TokenResponseDto login(MemberLoginRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject()
            .authenticate(authenticationToken);

        TokenSet tokenSet = tokenProvider.generateToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
            .key(authentication.getName())
            .value(tokenSet.getRefreshToken())
            .build();

        refreshTokenRepository.save(refreshToken);

        return TokenResponseDto.of(tokenSet);
    }

    @Transactional
    public TokenResponseDto reissue(TokenReissueRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token is not valid");
        }

        Authentication authentication = tokenProvider.getAuthentication(
            tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
            .orElseThrow(() -> new RuntimeException("Current user not exist"));

        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("token info not match");
        }

        TokenSet tokenSet = tokenProvider.generateToken(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenSet.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return TokenResponseDto.of(tokenSet);
    }
}
