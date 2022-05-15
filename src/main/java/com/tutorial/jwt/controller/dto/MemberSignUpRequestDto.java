package com.tutorial.jwt.controller.dto;


import com.tutorial.jwt.domain.Member;
import com.tutorial.jwt.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignUpRequestDto {

    private String username;
    private String password;
    private String email;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
            .userName(username)
            .password(passwordEncoder.encode(password))
            .email(email)
            .role(Role.USER)
            .build();
    }
}
