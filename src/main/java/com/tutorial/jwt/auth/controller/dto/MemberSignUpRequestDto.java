package com.tutorial.jwt.auth.controller.dto;


import com.tutorial.jwt.auth.domain.Member;
import com.tutorial.jwt.auth.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@ToString
@Getter
@Setter
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
