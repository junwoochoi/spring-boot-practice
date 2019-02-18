package com.example.junsta.posts;

import com.example.junsta.uploadImages.UploadedImage;
import com.example.junsta.uploadImages.UploadedImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}
