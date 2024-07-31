package org.jang.member.services;

import lombok.RequiredArgsConstructor;
import org.jang.member.MemberInfo;
import org.jang.member.constants.Authority;
import org.jang.member.entities.Authorities;
import org.jang.member.entities.Member;
import org.jang.member.repositories.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        //두번째 매개변수에 빈값일떄 들어감

        List<Authorities> tmp = Objects.requireNonNullElse(member.getAuthorities(),List.of(Authorities.builder().member(member).authority(Authority.USER).build()));//권한없을떄 설정

        List<SimpleGrantedAuthority> authorities = tmp.stream()
                .map(a -> new SimpleGrantedAuthority(a.getAuthority().name()))
                .toList();

        return MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();
    }
}
