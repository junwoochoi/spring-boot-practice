package com.example.junsta.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Account save(AccountDto dto){
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return accountRepository.save(dto.toEntity());
    }

    public List<Account> findAll(){
        return accountRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username));
        return new AccountAdapter(account);
    }



    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public void deleteAccount(Account account) {
        accountRepository.delete(account);
    }
}
