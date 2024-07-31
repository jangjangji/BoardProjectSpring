package org.jang.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override//3번째 매개변수 인증권한정보 담겨있음
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //세션에 남아있는 requestLogin 값 제거
        HttpSession session = request.getSession();


        session.removeAttribute("requestLogin");
            //로그인 성공시 - redirectUrl 이 있으면 해당 주소로 이동 아니면 메인페이지
        String redirectUrl = request.getParameter("redirectUrl");
        redirectUrl = StringUtils.hasText(redirectUrl) ? redirectUrl.trim() : "/";
        response.sendRedirect(request.getContextPath() + redirectUrl);


    }
}
