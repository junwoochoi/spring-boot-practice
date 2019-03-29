package com.example.junsta.common;


import com.example.junsta.accounts.Account;
import com.example.junsta.accounts.AccountRequestDto;
import com.example.junsta.accounts.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AppProperties appProperties;

    @Autowired
    protected AccountService accountService;


    protected Account createTestAccount() {
        Optional<Account> optionalAccount = accountService.findByEmail(appProperties.getTestEmail());
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        }

        AccountRequestDto dto = AccountRequestDto.builder()
                .displayName("test")
                .email(appProperties.getTestEmail())
                .password(appProperties.getTestPassword())
                .build();

        return accountService.save(dto);

    }

    protected String getAccessToken() throws Exception {
        createTestAccount();

        ResultActions perform = mockMvc.perform(
                post("/oauth/token")
                        .with(httpBasic(appProperties.getClientId(), appProperties.getSecret()))
                        .param("username", appProperties.getTestEmail())
                        .param("password", appProperties.getTestPassword())
                        .param("grant_type", "password")
        );
        String responseString = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return "Bearer " + parser.parseMap(responseString).get("access_token").toString();
    }
}
