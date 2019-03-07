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
@Table(name = "Account")
@EqualsAndHashCode(of = {"email"}, callSuper = false)
public class Account extends BaseEntity {


    @Column(name = "email",unique = true, nullable = false)
    @NaturalId
    private String email;

    @Column(name = "diplayName", nullable = false)
    private String displayName;

    @Column(name = "password",  nullable = false)
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

    public void updateAccount(AccountUpdateRequestDto dto) {
        this.displayName=dto.getDisplayName();
    }
}
