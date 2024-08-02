package org.jang.global.advices;

import lombok.RequiredArgsConstructor;
import org.jang.member.MemberUtil;
import org.jang.member.entities.Member;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice("org.jang")
@RequiredArgsConstructor
public class CommonControllerAdvice {
    private final MemberUtil memberUtil;
    @ModelAttribute("loggedMember")
    public Member loggedMember(){
        return memberUtil.getMember();
    }
    @ModelAttribute("isLogin")
    public boolean isLogin(){
        return memberUtil.isLogin();
    }
    @ModelAttribute("isAdmin")
    public boolean isAdmin(){
        return memberUtil.isAdmin();
    }
}
