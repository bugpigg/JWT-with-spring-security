package com.tutorial.jwt.controller.dto;

import com.tutorial.jwt.token.TokenSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TokenResponseDto {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;

    public static TokenResponseDto of(TokenSet tokenSet) {
        return new TokenResponseDto(
            TokenSet.BEARER_TYPE,
            tokenSet.getAccessToken(),
            tokenSet.getRefreshToken(),
            tokenSet.getAccessTokenExpiresIn().getTime()
        );
    }
}
