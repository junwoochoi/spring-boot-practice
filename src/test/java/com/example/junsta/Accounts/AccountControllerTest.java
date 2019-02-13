package com.example.junsta.Accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setup(){
        accountRepository.deleteAll();
    }

    @Test
    public void 회원가입_성공() throws Exception {

        String email = "junu4690@naver.com";
        String password = "password";
        String displayName = "displayName";
        AccountDto dto = AccountDto.builder()
                .email(email)
                .password(password)
                .displayName(displayName)
                .build();

        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(dto))
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value(email))
                .andExpect(jsonPath("displayName").value(displayName))
                .andExpect(jsonPath("password").doesNotExist());
    }

    @Test
    public void 회원가입_실패_아이디_공백() throws Exception {

        String email = " ";
        String password = "password";
        String displayName = "displayName";
        AccountDto dto = AccountDto.builder()
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
        AccountDto dto = AccountDto.builder()
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
        AccountDto dto = AccountDto.builder()
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

}