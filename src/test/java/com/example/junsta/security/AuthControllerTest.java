package com.example.junsta.security;

import com.example.junsta.accounts.Account;
import com.example.junsta.accounts.AccountDto;
import com.example.junsta.accounts.AccountRepository;
import com.example.junsta.accounts.AccountService;
import com.example.junsta.common.AppProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AppProperties appProperties;

    @Before
    public void 초기화(){
        accountRepository.deleteAll();
    }

    @Test
    public void 토큰발급() throws Exception {
        String email = "test@email.com";
        String password = "password";
        AccountDto dto = AccountDto.builder()
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                .andExpect(jsonPath("refresh_token").exists());
    }

    @Test
    public void 토큰발급_실패_비밀번호다름() throws Exception {
        String email = "test@email.com";
        String password = "password";
        AccountDto dto = AccountDto.builder()
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
        AccountDto dto = AccountDto.builder()
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
                .andExpect(status().isUnauthorized());
    }
}
