package com.example.junsta.comments;

import com.example.junsta.accounts.Account;
import com.example.junsta.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

    public CommentResponseDto saveComment(CommentRequestDto dto, Account currentUser) {
        Comment savedComment = commentRepository.save(dto.toEntity(currentUser, postService));

        return new CommentResponseDto(savedComment);
    }
}
