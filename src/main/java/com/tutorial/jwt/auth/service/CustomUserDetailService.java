package com.tutorial.jwt.auth.service;

import com.tutorial.jwt.auth.domain.Member;
import com.tutorial.jwt.auth.repository.MemberRepository;
import java.util.Collections;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUserName(username)
            .map(this::createUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException(username + "-> user not exist"));
    }

    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole());

        return new User(
            String.valueOf(member.getUserName()),
            member.getPassword(),
            Collections.singleton(grantedAuthority)
        );
    }
}
