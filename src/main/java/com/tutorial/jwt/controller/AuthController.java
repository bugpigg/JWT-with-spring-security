package com.tutorial.jwt.controller;

import com.tutorial.jwt.controller.dto.MemberLoginRequestDto;
import com.tutorial.jwt.controller.dto.TokenResponseDto;
import com.tutorial.jwt.controller.dto.MemberSignUpResponseDto;
import com.tutorial.jwt.controller.dto.TokenReissueRequestDto;
import com.tutorial.jwt.service.AuthService;
import com.tutorial.jwt.controller.dto.MemberSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberSignUpResponseDto> signUp(@RequestBody MemberSignUpRequestDto requestDto) {
        return ResponseEntity.ok(authService.signUp(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody MemberLoginRequestDto memberRequestDto) {
        return ResponseEntity.ok(authService.login(memberRequestDto));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@RequestBody TokenReissueRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }
}
