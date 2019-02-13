package com.example.junsta.Accounts;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class AccountDto {

    @NotEmpty
    private String displayName;
    @NotEmpty @Email
    private String email;
    @NotEmpty
    private String password;

    public Account toEntity(){
        return Account.builder()
                .displayName(this.displayName)
                .password(this.password)
                .email(this.email)
                .build();
    }
}
