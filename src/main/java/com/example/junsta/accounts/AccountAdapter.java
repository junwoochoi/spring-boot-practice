package com.example.junsta.accounts;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountAdapter extends User {

    @Getter
    private Account account;

    public AccountAdapter(Account account) {
        super(account.getEmail(), account.getPassword(), convertToAuthorities(account.getRoles()));
        this.account = account;
    }

    private static Collection<? extends GrantedAuthority> convertToAuthorities(Set<AccountRole> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
    }
}
