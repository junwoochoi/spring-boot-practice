package com.example.junsta.accounts;

import com.example.junsta.exceptions.MemberNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Account save(AccountRequestDto dto){
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

    public AccountResponseDto updateAccount(Long id, AccountUpdateRequestDto dto) throws MemberNotExistException {
        Optional<Account> optionalAccount = accountRepository.findByEmail(dto.getEmail());

        if(!optionalAccount.isPresent() || optionalAccount.get().getId() != id){
            throw new MemberNotExistException();
        }

        Account account = optionalAccount.get();

        account.updateAccount(dto);
        return new AccountResponseDto(account);
    }

}
