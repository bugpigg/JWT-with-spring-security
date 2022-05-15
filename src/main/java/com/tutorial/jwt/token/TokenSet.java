package com.tutorial.jwt.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@Slf4j
@Getter
@RequiredArgsConstructor
public class TokenSet {

    public static final String AUTHORITIES_KEY = "role";
    public static final String BEARER_TYPE = "bearer";

    private final Key key;

    private final Date accessTokenExpiresIn;
    private final String accessToken;
    private final String refreshToken;

    TokenSet(Key key, Authentication authentication, long accessExpireTime, long refreshExpireDate
    ) {
        this.key = key;
        this.accessTokenExpiresIn = getExpireTime(accessExpireTime);
        this.accessToken = createToken(authentication, this.accessTokenExpiresIn);
        this.refreshToken = createToken(getExpireTime(refreshExpireDate));
    }

    private Date getExpireTime(long expireTime) {
        return new Date(getCurrentTime() + expireTime);
    }

    private long getCurrentTime() {
        return (new Date()).getTime();
    }

    private String createToken(Authentication authentication, Date expireIn) {
        String roles = getRoles(authentication);

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, roles)
            .setExpiration(expireIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private String createToken(Date expireIn) {
        return Jwts.builder()
            .setExpiration(expireIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private String getRoles(Authentication authentication) {
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
    }
}
