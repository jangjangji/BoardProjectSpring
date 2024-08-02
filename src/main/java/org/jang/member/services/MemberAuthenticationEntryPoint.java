package org.jang.member.services;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class MemberAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //인가실패시 할 아이들
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println(authException);
        //InsufficientAuthenticationException
        /*회원 전용 페이지로 접근한경우 /mypage로 이동
         관리자 페이지로 접근한 경우 - 응답코드 401, 에러페이지 출력
         */
        String uri = request.getRequestURI();
        if (uri.contains("/admin")) {//관리자 페이지}
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

        } else {//회원전용페이지
            String gs = request.getQueryString();
            String redierctUrl = uri.replace(request.getContextPath(),"");
          if(StringUtils.hasText(gs)){
              redierctUrl += "?" + gs;
          }
            response.sendRedirect(request.getContextPath() + "/member/login?redirectUrl=" + redierctUrl);
        }
    }
}
