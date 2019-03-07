package com.example.junsta.posts;

import com.example.junsta.accounts.Account;
import com.example.junsta.exceptions.PostNotExistException;
import com.example.junsta.exceptions.UnauthorizedException;
import com.example.junsta.uploadImages.UploadedImage;
import com.example.junsta.uploadImages.UploadedImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PostService {

    @Autowired
    private UploadedImageRepository uploadedImageRepository;

    @Autowired
    private PostRepository postRepository;

    public PostResponseDto uploadPost(PostRequestDto dto) {
        UploadedImage uploadedImage = dto.getUploadImageEntity();
        uploadedImageRepository.save(uploadedImage);


        Post post = postRepository.save(dto.getPostEntity());

        return new PostResponseDto(post);
    }

    public Page<PostResponseDto> findAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);

        return posts.map((post -> new PostResponseDto(post)));
    }

    public PostResponseDto updatePost(PostUpdateRequestDto dto, Account account) {
        Post post = postRepository.findById(dto.getId()).orElseThrow(PostNotExistException::new);

        if(!post.getAccount().equals(account)){
            throw new UnauthorizedException();
        }

        post.updateText(dto);

        return new PostResponseDto(post);
    }
}
