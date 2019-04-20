package com.example.junsta.posts;

import com.example.junsta.accounts.Account;
import com.example.junsta.common.S3ImageRemover;
import com.example.junsta.exceptions.PostNotExistException;
import com.example.junsta.exceptions.UnauthorizedException;
import com.example.junsta.uploadImages.UploadedImage;
import com.example.junsta.uploadImages.UploadedImageRepository;
import com.example.junsta.uploadImages.UploadedImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;
    private final UploadedImageService uploadedImageService;
    private final S3ImageRemover s3ImageRemover;


    public PostResponseDto uploadPost(PostRequestDto dto) {

        Post post = Post.builder()
                .account(dto.getAccount())
                .uploadedImage(uploadedImageService.findByImageName(dto.getUploadedImageName()).get())
                .postText(dto.getPostText())
                .build();

        Post savedPost = postRepository.save(post);

        return new PostResponseDto(savedPost);
    }

    public Page<PostResponseDto> findAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);

        return posts.map((PostResponseDto::new));
    }

    public Optional<Post> findById(Long id){
        return postRepository.findById(id);
    }

    public PostResponseDto updatePost(PostUpdateRequestDto dto, Account account) {
        Post post = findPostIfExists(dto.getId(), account);

        post.updateText(dto);

        return new PostResponseDto(post);
    }


    public void deletePost(Long id, Account currentUser) {
        Post post = findPostIfExists(id, currentUser);
        UploadedImage image = post.getUploadedImage();
        s3ImageRemover.deleteFromS3(image);
        postRepository.delete(post);
        uploadedImageService.delete(image.getId());
    }

    public void createLike(Long postId, Account currentUser) {
        Post post = findPostIfExists(postId, currentUser);

        post.createLike(currentUser);
    }

    private Post findPostIfExists(Long postId, Account currentUser) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotExistException::new);
        if (!currentUser.equals(post.getAccount())) {
            throw new UnauthorizedException();
        }
        return post;
    }
}
