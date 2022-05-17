package com.tutorial.jwt.auth.token;

import static com.tutorial.jwt.auth.token.TokenSet.AUTHORITIES_KEY;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Slf4j
public class TokenProvider {

    private final Key key;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;

    public TokenProvider(String secret, long accessTokenExpireTime, long refreshTokenExpireTime) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public TokenSet generateToken(Authentication authentication) {
        return new TokenSet(key, authentication, accessTokenExpireTime, refreshTokenExpireTime);
    }

    public Authentication getAuthentication(String accessToken) {

        if (validateToken(accessToken)) {
            Claims tokenClaims = parseClaims(accessToken);
            Collection<? extends GrantedAuthority> authorities = Arrays.stream(
                    tokenClaims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

            User user = new User(tokenClaims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(user, "", authorities);
        } else {
            throw new RuntimeException("Token Validation Failed");
        }
    }

    public boolean validateToken(String token) {
       return parseClaims(token) != null;
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            return e.getClaims();
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }
}
