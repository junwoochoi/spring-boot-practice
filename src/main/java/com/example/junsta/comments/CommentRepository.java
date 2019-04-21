package com.example.junsta.comments;

import com.example.junsta.posts.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPost(Post post, Pageable pageable);
    Page<Comment> findAllByPostAndParentCommentIsNull(Post post, Pageable pageable);
}
