package com.example.junsta.comments;

import com.example.junsta.accounts.Account;
import com.example.junsta.exceptions.CommentNotExistException;
import com.example.junsta.exceptions.PostNotExistException;
import com.example.junsta.exceptions.UnauthorizedException;
import com.example.junsta.posts.Post;
import com.example.junsta.posts.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

    public CommentResponseDto saveComment(CommentPostRequestDto dto) {
        Comment savedComment = commentRepository.save(dto.toEntity(postService, commentRepository));

        return new CommentResponseDto(savedComment);
    }

    public Page<CommentResponseDto> findByPostId(Long postId, Pageable pageable) {
        Post post = postService.findById(postId).orElseThrow(PostNotExistException::new);
        return commentRepository.findAllByPost(post, pageable).map(CommentResponseDto::new);
    }


    public Page<CommentResponseDto> findAllByPostAndParentCommentIsNull(Long postId, Pageable pageable) {
        Post post = postService.findById(postId).orElseThrow(PostNotExistException::new);
        return commentRepository.findAllByPostAndParentCommentIsNull(post, pageable).map(CommentResponseDto::new);
    }

    public CommentResponseDto updateComment(CommentPutRequestDto dto, Account currentUser) {
        Comment comment = commentRepository.findById(dto.getCommentId()).orElseThrow(CommentNotExistException::new);

        if(!comment.getCreatedBy().equals(currentUser)){
            throw new UnauthorizedException();
        }

        comment.updateText(dto);
        commentRepository.saveAndFlush(comment);
        return new CommentResponseDto(comment);
    }

    public Comment findByCommentId(Long paretnCommentId) {
        return commentRepository.findById(paretnCommentId).orElseThrow(CommentNotExistException::new);
    }
}
