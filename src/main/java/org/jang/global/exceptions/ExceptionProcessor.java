package org.jang.global.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.jang.global.exceptions.script.AlertBackException;
import org.jang.global.exceptions.script.AlertException;
import org.jang.global.exceptions.script.AlertRedierctException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

public interface ExceptionProcessor {
    @ExceptionHandler(Exception.class)
    default ModelAndView errorHandler(Exception e, HttpServletRequest request) {

        ModelAndView mv = new ModelAndView();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 기본 응답 코드 500
        String tpl = "error/error";

        if (e instanceof CommonException commonException) {
            status = commonException.getStatus();

            if (e instanceof AlertException) {
                tpl = "common/_execute_script";
                String script = String.format("alert('%s');", e.getMessage());

                if (e instanceof AlertBackException alertBackException) {
                    script += String.format("%s.history.back();", alertBackException.getTarget());
                }
                if(e instanceof AlertRedierctException alertRedierctException) {
                    String url = alertRedierctException.getUrl();
                    if(!url.startsWith("http")) {//외부 url 이 아닌경우
                        url = request.getContextPath() + url;
                    }
                    script += String.format("%s.location.replace('%s);", alertRedierctException.getTarget(),url);
                }
                mv.addObject("script", script);
            }

        }

        String url = request.getRequestURI();
        String qs = request.getQueryString();

        if (StringUtils.hasText(qs)) url += "?" + qs;


        mv.addObject("message", e.getMessage());
        mv.addObject("status", status.value());
        mv.addObject("method", request.getMethod());
        mv.addObject("path", url);
        mv.setStatus(status);
        mv.setViewName(tpl);


        return mv;
    }
}