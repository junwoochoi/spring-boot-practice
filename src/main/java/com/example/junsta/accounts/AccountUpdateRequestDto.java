package com.example.junsta.accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AccountUpdateRequestDto {

    @NotEmpty
    @NotBlank
    private String displayName;
    @NotEmpty @NotBlank @Email
    private String email;
}
