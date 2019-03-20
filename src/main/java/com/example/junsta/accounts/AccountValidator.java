package com.example.junsta.accounts;

import com.example.junsta.exceptions.AccountAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class AccountValidator {

    @Autowired
    AccountService accountService;

    public void validate(AccountRequestDto dto){
        if(accountService.findByEmail(dto.getEmail()).isPresent()){
            throw new AccountAlreadyExistException();
        }
    }
}
