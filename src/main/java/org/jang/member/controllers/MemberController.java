package org.jang.member.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jang.global.exceptions.ExceptionProcessor;
import org.jang.member.MemberInfo;
import org.jang.member.services.MemberSaveService;
import org.jang.member.validators.JoinValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@SessionAttributes("requestLogin")
public class MemberController implements ExceptionProcessor {

    private final JoinValidator joinValidator;
    private final MemberSaveService memberSaveService;


    @ModelAttribute
    public RequestLogin requestLogin() {
        return new RequestLogin();
    }

    @GetMapping("/join")
    public String join(@ModelAttribute RequestJoin form, Model model) {
        commonProcess("join", model);

        return "front/member/join";
    }

    @PostMapping("/join")
    public String joinPs(@Valid RequestJoin form, Errors errors, Model model) {
        joinValidator.validate(form, errors);

        commonProcess("join", model);

        if (errors.hasErrors()) {
            return "front/member/join";
        }

        memberSaveService.save(form); // 회원 가입 처리

        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String login(@Valid @ModelAttribute RequestLogin form, Errors errors, Model model) {
        String code = form.getCode();
        commonProcess("login", model);
        if (StringUtils.hasText(code)) {
            errors.reject(code, form.getDefaultMessage());

            // 비번 만료인 경우 비번 재설정 페이지 이동
            if (code.equals("CredentialsExpired.Login")) {
                return "redirect:/member/password/reset";
            }
        }

        return "front/member/login";
    }



    @ResponseBody
    @GetMapping("/test")
    public void test(Principal principal) {
        log.info("로그인 아이디: {}", principal.getName());
    }

    @ResponseBody
    @GetMapping("/test2")
    public void test2(@AuthenticationPrincipal MemberInfo memberInfo) {
        log.info("로그인 회원: {}", memberInfo.toString());
    }

    @ResponseBody
    @GetMapping("/test3")
    public void test3() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();



        log.info("로그인 상태: {}", authentication.isAuthenticated());
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberInfo) { // 로그인 상태 - UserDetails 구현체(getPrincipal())
            MemberInfo memberInfo = (MemberInfo) authentication.getPrincipal();
            log.info("로그인 회원: {}", memberInfo.toString());
        } else { // 미로그인 상태 - String / anonymousUser (getPrincipal())
            log.info("getPrincipal(): {}", authentication.getPrincipal());
        }
    }

    /*
    공통된 스타일 관련
     */

    private void commonProcess(String mode, Model model) {
        mode = Objects.requireNonNullElse(mode, " join");

        List<String> addCss = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        addCss.add("member/style");
        if(mode.equals("join")){
            addCommonScript.add("fileManager");
            addCss.add("member/join");
            addScript.add("member/join");
        } else if (mode.equals("login")) {
            addCss.add("member/login");
        }
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("addCss", addCss);
    }
}