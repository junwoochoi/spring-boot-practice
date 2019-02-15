package com.example.junsta.accounts;

import com.example.junsta.common.BaseEntity;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Account extends BaseEntity {


    @Column(unique = true, nullable = false)
    @NaturalId
    private String email;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<AccountRole> roles = Stream.of(AccountRole.ROLE_USER).collect(Collectors.toSet());

    @Builder
    public Account(String email, String displayName, String password){
        this.email=email;
        this.displayName=displayName;
        this.password=password;
    }
}
