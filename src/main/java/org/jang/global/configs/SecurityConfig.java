package org.jang.global.configs;

import org.jang.member.services.LoginFailureHandler;
import org.jang.member.services.LoginSuccessHandler;
import org.jang.member.services.MemberAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //로그인
        http.formLogin(f -> {
            f.loginPage("/member/login")
                    .usernameParameter("email")
                   .passwordParameter("password")
                    .successHandler(new LoginSuccessHandler())
                    .failureHandler(new LoginFailureHandler());
        });
        http.logout(f -> {
            f.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                    .logoutSuccessUrl("/member/login");
        });
        http.authorizeHttpRequests(c->{
            /*
            //회원전용 페이지
           c.requestMatchers("/member/**").anonymous()
                   .requestMatchers("/admin/**").hasAnyAuthority()
                   .anyRequest().authenticated();

           */
            c.requestMatchers("/mypage/**").authenticated() //회원전용
                    .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                    .anyRequest().permitAll();
        });
        http.exceptionHandling(c->{
           c.authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                   .accessDeniedHandler((req,res,e)->{
                    res.sendError(HttpStatus.UNAUTHORIZED.value());
            });
            //권한이 부족하여 요청을 처리할 없을때 호출되는 핸들러를 설정하는거임 응답에 HTTP 상태코드가 401을 설정  이 핸들러는 인증된 사용자가 접근할 수 있는 권한이 없는 리소스에 접근하려 할 때 적절한 오류 응답을 반환합니다.
        });
        //iframe 자원 출처를 같은 서버 자원으로 한정
        http.headers(c-> c.frameOptions(f->f.sameOrigin()));
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
