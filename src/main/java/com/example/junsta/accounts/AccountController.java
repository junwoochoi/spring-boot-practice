package com.example.junsta.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping
    public ResponseEntity createAccount(@RequestBody @Valid AccountRequestDto dto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Account account = accountService.save(dto);


        return ResponseEntity.ok(new AccountResponseDto(account));
    }

    @DeleteMapping
    public ResponseEntity deleteAccount(@AuthenticationPrincipal AccountAdapter accountAdapter){
        Account account = accountAdapter.getAccount();
        Optional<Account> accountOptional = accountService.findByEmail(account.getEmail());

        if(!accountOptional.isPresent()){
            return ResponseEntity.badRequest().build();
        }

        accountService.deleteAccount(account);
        return ResponseEntity.ok().build();
    }

}
