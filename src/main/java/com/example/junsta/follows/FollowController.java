package com.example.junsta.follows;


import com.example.junsta.accounts.Account;
import com.example.junsta.accounts.AccountAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowController{

    private final FollowService followService;

    @PostMapping
    public ResponseEntity startFollow(@RequestBody FollowDto dto
            , @AuthenticationPrincipal AccountAdapter accountAdapter){
        Account currentUser = accountAdapter.getAccount();
        followService.startFollow(dto, currentUser);
        return ResponseEntity.noContent().build();
    }

}
