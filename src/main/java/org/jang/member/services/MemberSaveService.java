package org.jang.member.services;

import lombok.RequiredArgsConstructor;
import org.jang.member.constants.Authority;
import org.jang.member.controllers.RequestJoin;
import org.jang.member.entities.Authorities;
import org.jang.member.entities.Member;
import org.jang.member.repositories.AuthoritiesRepository;
import org.jang.member.repositories.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberSaveService {
    private final MemberRepository memberRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입 처리
    public void save(RequestJoin form){
        Member member = new ModelMapper().map(form, Member.class);
        String hash = passwordEncoder.encode(form.getPassword());
        member.setPassword(hash);

        save(member, List.of(Authority.USER));

    }

    public void save(Member member, List<Authority> authorities){
        String mobile = member.getMobile();
        if(StringUtils.hasText(mobile)){
            mobile = mobile.replaceAll("\\D","");
            member.setMobile(mobile);
        }
        memberRepository.saveAndFlush(member);
        if (authorities != null) {
            List<Authorities> items =  authoritiesRepository.findByMember(member);
            authoritiesRepository.deleteAll(items);
            authoritiesRepository.flush();
            items = authorities.stream().map(a->Authorities.builder()
                    .member(member)
                    .authority(a)
                    .build()).toList();

            authoritiesRepository.saveAllAndFlush(items);
        }
    }
}
