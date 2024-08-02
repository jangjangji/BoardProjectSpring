package org.jang.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class secu {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void test1() throws Exception {
        mockMvc.perform(post("/member/join")
                        .with(csrf().asHeader())
                .param("email", "sjjang41@naver.com"))
                .andDo(print());
    }
    @Test
    @WithMockUser
    void test2() throws Exception{
        mockMvc.perform(get("/mypage"))
                .andDo(print());
    }
    @Test
    @WithMockUser(username = "user01@test.org" ,authorities="ADMIN")
    void test3() throws Exception {
        mockMvc.perform(get("/admin"))
                .andDo(print());
    }
}
