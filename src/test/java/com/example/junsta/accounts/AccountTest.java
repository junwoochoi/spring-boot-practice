package com.example.junsta.accounts;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AccountTest {

    @Autowired
    AccountRepository accountRepository;

    @Before
    public void cleanup() {
        accountRepository.deleteAll();
    }


    @Test
    public void 어카운트_생성_테스트() {

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
    public void 팔로우_테스트() {
        Account junu = createAccount("junu@email.com", "최주누", "비번");
        Account stalker = createAccount("stalker@email.com", "스토커", "비번");

        junu.startFollow(stalker);


        assertThat(junu.getFollowingSet().size()).isGreaterThan(0);
        assertThat(stalker.getBeFollowedSet().size()).isGreaterThan(0);

    }

    @Transactional
    @Test
    public void 수정시간_확인() {
        Account junu = createAccount("junu@email.com", "최주누", "비번");

        String displayName = "hello@";
        junu.updateAccount(
                AccountUpdateRequestDto.builder()
                        .displayName(displayName)
                        .build()
        );


        assertThat(junu.getDisplayName()).isEqualTo(displayName);
        assertThat(junu.getCreatedAt()).isBefore(junu.getModifiedAt());
    }


    private Account createAccount(String email, String displayName, String password) {
        return accountRepository.save(Account.builder()
                .email(email)
                .displayName(displayName)
                .password(password)
                .build());
    }
}