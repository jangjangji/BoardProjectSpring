package org.jang.member;

import lombok.Builder;
import lombok.Data;
import org.jang.member.entities.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
@Data
@Builder
public class MemberInfo implements UserDetails {
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    //<? extends GrantedAuthority> 상속하는 어떤 타입이든 올수있음
    //authorities: 이 변수는 사용자 또는 객체의 권한을 나타내는 GrantedAuthority 객체들을 저장하는 Collection입니다
    private Member member;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //계정이 잠겨있는지 확인
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override // false 이면 로그인 불가 상태
    public boolean isEnabled() {
        return true;
    }
}
