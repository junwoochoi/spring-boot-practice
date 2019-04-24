package com.example.junsta.follows;


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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FollowControllerTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

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
                .andExpect(
                        status().isNoContent()
                );

        assertThat(testAccount.getFollowingSet().contains(savedUser)).isTrue();
        assertThat(savedUser.getBeFollowedSet().contains(testAccount)).isTrue();

    }

}