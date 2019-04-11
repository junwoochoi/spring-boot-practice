package com.example.junsta.config;

import com.example.junsta.accounts.Account;
import com.example.junsta.accounts.AccountAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<Account> auditorAware(){
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")){
                return Optional.empty();
            }
            AccountAdapter accountAdapter = (AccountAdapter) authentication.getPrincipal();
            return Optional.of(accountAdapter.getAccount());
        };
    }
}
