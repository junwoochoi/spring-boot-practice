package com.example.junsta.accounts;

import com.example.junsta.common.BaseEntity;
import com.example.junsta.posts.Post;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "account")
@EqualsAndHashCode(of = {"email"}, callSuper = false)
public class Account extends BaseEntity {


    @Column(name = "email",unique = true, nullable = false)
    @NaturalId
    private String email;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "password",  nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<AccountRole> roles = Stream.of(AccountRole.ROLE_USER).collect(Collectors.toSet());

    @ManyToMany
    @JoinTable(name = "follow",
            joinColumns = @JoinColumn(name = "following_user"),
            inverseJoinColumns = @JoinColumn(name = "followed_user"))
    private Set<Account> followingSet = new HashSet<>();

    @ManyToMany(mappedBy = "followingSet")
    private Set<Account> beFollowedSet = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "like",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> likePosts = new HashSet<>();

    @Builder
    public Account(String email, String displayName, String password){
        this.email=email;
        this.displayName=displayName;
        this.password=password;
    }

    public void updateAccount(AccountUpdateRequestDto dto) {
        this.displayName=dto.getDisplayName();
    }

    public void startFollow(Account followedUser) {
        this.followingSet.add(followedUser);
        followedUser.getBeFollowedSet().add(this);
    }

    public void cancelFollow(Account followedUser) {
        this.followingSet.remove(followedUser);
        followedUser.getBeFollowedSet().remove(this);
    }
}
