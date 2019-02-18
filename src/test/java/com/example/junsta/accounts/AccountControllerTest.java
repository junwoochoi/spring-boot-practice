package com.example.junsta.accounts;

import com.example.junsta.common.BaseControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class AccountControllerTest extends BaseControllerTest {



    @Autowired
    AccountRepository accountRepository;


    @Before
    public void setup() {
        accountRepository.deleteAll();
    }

    @Test
    public void 회원가입_성공() throws Exception {

        String email = "junu4690@naver.com";
        String password = "password";
        String displayName = "displayName";
        AccountRequestDto dto = AccountRequestDto.builder()
                .email(email)
                .password(password)
                .displayName(displayName)
                .build();

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(dto))
        ).andDo(print())
                .andDo(document("create-account",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입 헤더"),
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더")
                        ),
                        requestFields(
                                fieldWithPath("displayName").description("회원 닉네임"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("password").description("회원 비밀번호")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입 헤더")
                        ),
                        responseFields(
                                fieldWithPath("displayName").description("회원가입된 회원 닉네임"),
                                fieldWithPath("email").description("회원가입된 회원 이메일")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("displayName").value(displayName))
                .andExpect(jsonPath("password").doesNotHaveJsonPath());
    }

    @Test
    public void 회원가입_실패_아이디_공백() throws Exception {

        String email = " ";
        String password = "password";
        String displayName = "displayName";
        AccountRequestDto dto = AccountRequestDto.builder()
                .email(email)
                .password(password)
                .displayName(displayName)
                .build();

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(dto))
        ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입_실패_비밀번호_공백() throws Exception {

        String email = "email@naver.com";
        String password = "  ";
        String displayName = "displayName";
        AccountRequestDto dto = AccountRequestDto.builder()
                .email(email)
                .password(password)
                .displayName(displayName)
                .build();

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(dto))
        ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입_실패_닉네임_공백() throws Exception {

        String email = "email@naver.com";
        String password = "password";
        String displayName = "  ";
        AccountRequestDto dto = AccountRequestDto.builder()
                .email(email)
                .password(password)
                .displayName(displayName)
                .build();

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(dto))
        ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 토큰받기_성공() throws Exception {
        createTestAccount();

        mockMvc.perform(
                post("/oauth/token")
                        .with(httpBasic(appProperties.getClientId(), appProperties.getSecret()))
                        .param("username", appProperties.getTestEmail())
                        .param("password", appProperties.getTestPassword())
                        .param("grant_type", "password")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                .andExpect(jsonPath("refresh_token").exists());
    }

    @Test
    public void 회원탈퇴() throws Exception {

        mockMvc.perform(
                delete("/api/accounts")
                        .header(HttpHeaders.AUTHORIZATION,  getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/oauth/token")
                        .with(httpBasic(appProperties.getClientId(), appProperties.getSecret()))
                        .param("username", appProperties.getTestEmail())
                        .param("password", appProperties.getTestPassword())
                        .param("grant_type", "password")
        ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("access_token").doesNotExist());
    }

    @Test
    public void 회원탈퇴_실패() throws Exception {

        mockMvc.perform(
                delete("/api/accounts")
                        .header(HttpHeaders.AUTHORIZATION,  getAccessToken()+"1234")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(print())
                .andExpect(status().isUnauthorized());

        mockMvc.perform(
                post("/oauth/token")
                        .with(httpBasic(appProperties.getClientId(), appProperties.getSecret()))
                        .param("username", appProperties.getTestEmail())
                        .param("password", appProperties.getTestPassword())
                        .param("grant_type", "password")
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }




}