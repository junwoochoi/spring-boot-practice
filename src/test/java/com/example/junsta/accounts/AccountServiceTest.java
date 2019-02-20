package com.example.junsta.accounts;


import com.example.junsta.common.AppProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AccountServiceTest {


    @Autowired
    AccountService accountService;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    public void 어카운트_저장_및_암호화여부(){
        String displayName = "닉네임";
        String email = "email@email.com";
        String password = "password";
        AccountRequestDto dto = AccountRequestDto.builder()
                .displayName(displayName)
                .email(email)
                .password(password)
                .build();

        accountService.save(dto);

        Account account = accountService.findByEmail(email).get();

        assertThat(account.getEmail()).isEqualTo(email);
        assertThat(account.getDisplayName()).isEqualTo(displayName);
        assertThat(passwordEncoder.matches(password, account.getPassword())).isTrue();

    }
}