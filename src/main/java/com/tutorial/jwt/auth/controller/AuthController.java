package com.tutorial.jwt.auth.controller;

import com.tutorial.jwt.auth.controller.dto.MemberLoginRequestDto;
import com.tutorial.jwt.auth.controller.dto.MemberSignUpRequestDto;
import com.tutorial.jwt.auth.controller.dto.MemberSignUpResponseDto;
import com.tutorial.jwt.auth.controller.dto.TokenReissueRequestDto;
import com.tutorial.jwt.auth.controller.dto.TokenResponseDto;
import com.tutorial.jwt.auth.service.AuthService;
import com.tutorial.jwt.auth.util.HeaderParser;
import com.tutorial.jwt.auth.util.CookieUtil;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberSignUpResponseDto> signUp(
        @RequestBody MemberSignUpRequestDto requestDto) {
        log.info("✅ Member SignUp request ---> {}", requestDto);
        return ResponseEntity.ok(authService.signUp(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(HttpServletResponse response,
        @RequestBody MemberLoginRequestDto memberRequestDto) {
        log.info("✅ Member Login request ---> {}", memberRequestDto);
        TokenResponseDto responseDto = authService.login(memberRequestDto);
        CookieUtil.addCookie(response, responseDto.getGrantType(), responseDto.getRefreshToken(),
            (int) (responseDto.getRefreshTokenExpiresIn() / 1000));
        HeaderParser.setAccessTokenInHeader(response, responseDto.getAccessToken());
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@RequestBody TokenReissueRequestDto tokenRequestDto) {
        log.info("✅ Token Reissue request ---> {}", tokenRequestDto);
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }
}
