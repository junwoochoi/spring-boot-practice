package com.example.junsta.follows;


import akka.http.javadsl.Http;
import com.example.junsta.accounts.Account;
import com.example.junsta.accounts.AccountRequestDto;
import com.example.junsta.accounts.AccountService;
import com.example.junsta.common.BaseControllerTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FollowControllerTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    FollowService followService;

    @Test
    public void 팔로우_시작_성공() throws Exception {
        Account testAccount = createTestAccount();

        String email = "junwoo4690@naver.com";
        String displayName = "최준우";
        String password = "password";

        AccountRequestDto dto = AccountRequestDto.builder()
                .displayName(displayName)
                .email(email)
                .password(password)
                .build();

        Account savedUser = accountService.save(dto);

        FollowDto followDto = FollowDto.builder()
                .email(email)
                .build();

        mockMvc.perform(
                post("/api/follows")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(followDto))
        )
                .andDo(print())
                .andDo(
                        document("start-follow",
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증 정보"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입")
                                ),
                                requestFields(
                                        fieldWithPath("email").description("팔로우 시작할 계정 이메일")
                                ))
                )
                .andExpect(
                        status().isNoContent()
                );

        assertThat(testAccount.getFollowingSet().contains(savedUser)).isTrue();
        assertThat(savedUser.getBeFollowedSet().contains(testAccount)).isTrue();

    }

    @Test
    public void 팔로우_시작_실패_존재하지_않는_멤버() throws Exception {
        Account testAccount = createTestAccount();


        FollowDto followDto = FollowDto.builder()
                .email("dsaflajsdflkja1208@nave.com")
                .build();

        mockMvc.perform(
                post("/api/follows")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(followDto))
        )
                .andExpect(
                        status().isBadRequest()
                );

    }


    @Test
    public void 팔로우_취소_성공() throws Exception {
        Account testAccount = createTestAccount();

        String email = "junwoo4690@naver.com";
        String displayName = "최준우";
        String password = "password";

        AccountRequestDto dto = AccountRequestDto.builder()
                .displayName(displayName)
                .email(email)
                .password(password)
                .build();

        Account savedUser = accountService.save(dto);

        FollowDto followDto = FollowDto.builder()
                .email(email)
                .build();
        followService.startFollow(followDto, testAccount);

        assertThat(testAccount.getFollowingSet().contains(savedUser)).isTrue();
        assertThat(savedUser.getBeFollowedSet().contains(testAccount)).isTrue();

        mockMvc.perform(
                delete("/api/follows")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(followDto))
        )
                .andDo(print())
                .andDo(
                        document("cancel-follow",
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증 정보"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입")
                                ),
                                requestFields(
                                        fieldWithPath("email").description("팔로우 취소할 계정 이메일")
                                ))
                )
                .andExpect(status().isNoContent());

        assertThat(testAccount.getFollowingSet().contains(savedUser)).isFalse();
        assertThat(savedUser.getBeFollowedSet().contains(testAccount)).isFalse();

    }



}