package com.example.junsta.follows;

import com.example.junsta.accounts.Account;
import com.example.junsta.accounts.AccountRepository;
import com.example.junsta.exceptions.MemberNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final AccountRepository accountRepository;

    public void startFollow(FollowDto dto, Account currentUser) {
        Account followedUser = accountRepository.findByEmail(dto.getEmail()).orElseThrow(MemberNotExistException::new);

        currentUser.startFollow(followedUser);
    }
}
