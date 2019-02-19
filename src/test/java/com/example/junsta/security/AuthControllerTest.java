package com.example.junsta.security;

import com.example.junsta.accounts.Account;
import com.example.junsta.accounts.AccountRepository;
import com.example.junsta.accounts.AccountRequestDto;
import com.example.junsta.accounts.AccountService;
import com.example.junsta.common.AppProperties;
import com.example.junsta.common.BaseControllerTest;
import com.example.junsta.posts.PostRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



public class AuthControllerTest extends BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    AppProperties appProperties;

    @Before
    public void 초기화(){
        postRepository.deleteAll();
        accountRepository.deleteAll();
        accountRepository.flush();
    }

    @Test
    public void 토큰발급() throws Exception {
        String email = "test@email.com";
        String password = "password";
        AccountRequestDto dto = AccountRequestDto.builder()
                .displayName("닉네임")
                .email(email)
                .password(password)
                .build();

        Account account = accountService.save(dto);


        mockMvc.perform(post("/oauth/token")
        .with(httpBasic(appProperties.getClientId(),  appProperties.getSecret()))
                .param("username", email)
                .param("password", password)
                .param("grant_type", "password")
        ).andDo(print())
                .andDo(document(
                        "get-token",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 정보 헤더")
                        ),
                        requestParameters(
                                parameterWithName("username").description("회원 이메일"),
                                parameterWithName("password").description("회원 비밀번호"),
                                parameterWithName("grant_type").description("회원 인증 방식 ( password, access_token )")
                        ),
                        responseFields(
                                fieldWithPath("access_token").description("액세스 토큰"),
                                fieldWithPath("token_type").description("토큰 타입"),
                                fieldWithPath("refresh_token").description("리프레시 토큰"),
                                fieldWithPath("expires_in").description("토큰 유효기간"),
                                fieldWithPath("scope").description("스코프")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                .andExpect(jsonPath("refresh_token").exists());
    }

    @Test
    public void 토큰발급_실패_비밀번호다름() throws Exception {
        String email = "test@email.com";
        String password = "password";
        AccountRequestDto dto = AccountRequestDto.builder()
                .displayName("닉네임")
                .email(email)
                .password(password)
                .build();

        Account account = accountService.save(dto);


        mockMvc.perform(post("/oauth/token")
                .with(httpBasic("client",  "secret"))
                .param("username", email)
                .param("password", "틀린패스워드")
                .param("grant_type", "password")
        ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    public void 토큰발급_실패_이메일_미존재() throws Exception {
        String email = "test@email.com";
        String password = "password";
        AccountRequestDto dto = AccountRequestDto.builder()
                .displayName("닉네임")
                .email(email)
                .password(password)
                .build();

        Account account = accountService.save(dto);


        mockMvc.perform(post("/oauth/token")
                .with(httpBasic("client",  "secret"))
                .param("username", email+"_미존재")
                .param("password", password)
                .param("grant_type", "password")
        ).andDo(print())
                .andDo(
                        document("oauth-authorization")
                )
                .andExpect(status().isUnauthorized());
    }
}
