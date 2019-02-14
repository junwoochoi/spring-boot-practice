package com.example.junsta.accounts;

import lombok.*;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AccountDto {

    @NotEmpty @NotBlank
    private String displayName;
    @NotEmpty @NotBlank @Email
    private String email;
    @NotEmpty
    @NotBlank
    @JsonIgnore
    private String password;

    public Account toEntity(){
        return Account.builder()
                .displayName(this.displayName)
                .password(this.password)
                .email(this.email)
                .build();
    }
}
