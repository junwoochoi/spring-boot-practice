package com.example.junsta.accounts;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:aws.properties")
public class AccountTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Before
    public void cleanup(){
        accountRepository.deleteAll();
    }

    @Test
    public void 어카운트_생성_테스트(){

        //Given
        String email = "junwoo4690@naver.com";
        String displayName = "최준우";
        String password = "password";

        Account test = createAccount(email, displayName, password);

        accountRepository.save(test);

        //when
        Account account = accountRepository.findAll().get(0);

        //then
        assertThat(account.getEmail()).isEqualTo(email);
        assertThat(account.getDisplayName()).isEqualTo(displayName);
        assertThat(account.getPassword()).isEqualTo(password);

    }

    @Test
    public void 팔로우_테스트(){
        Account junu = createAccount("junu@email.com","최주누", "비번");
        Account stalker = createAccount("stalker@email.com","스토커", "비번");

        junu.startFollow(stalker);


        assertThat(junu.getFollowingSet().size()).isGreaterThan(0);
        assertThat(stalker.getBeFollowedSet().size()).isGreaterThan(0);

    }


    private Account createAccount(String email, String displayName, String password) {
        return Account.builder()
                    .email(email)
                    .displayName(displayName)
                    .password(password)
                    .build();
    }
}