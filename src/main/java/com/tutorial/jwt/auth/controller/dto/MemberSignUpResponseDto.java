package com.tutorial.jwt.auth.controller.dto;

import com.tutorial.jwt.auth.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSignUpResponseDto {

    private String userName;

    public static MemberSignUpResponseDto of(Member member) {
        return new MemberSignUpResponseDto(member.getUserName());
    }
}
