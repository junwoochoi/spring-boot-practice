package com.example.junsta.comments;

import com.example.junsta.common.BaseControllerTest;
import com.example.junsta.posts.Post;
import com.example.junsta.posts.PostRepository;
import com.example.junsta.uploadImages.UploadedImage;
import com.example.junsta.uploadImages.UploadedImageRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CommentControllerTest extends BaseControllerTest {

    @Autowired
    CommentService commentService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UploadedImageRepository uploadedImageRepository;

    @Before
    public void setup(){
        createTestAccount();
    }

    @Test
    public void 댓글달기_성공() throws Exception {
        UploadedImage uploadedImage = UploadedImage.builder()
                .imageExtension("png")
                .imageName(UUID.randomUUID().toString())
                .imagePath("asdafasfasfasfd")
                .originalName("originalName")
                .account(createTestAccount())
                .build();
        uploadedImageRepository.save(uploadedImage);

        Post post = Post.builder()
                .postText("Hello Im Post blabla")
                .account(createTestAccount())
                .uploadedImage(uploadedImage)
                .build();
        postRepository.save(post);

        CommentRequestDto dto = CommentRequestDto.builder()
                .postId(post.getId())
                .commentText("this is comment contents")
                .build();

        mockMvc.perform(post("/api/comments")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("commentText").exists())
                .andExpect(jsonPath("createdBy").exists())
                .andExpect(jsonPath("createdAt").exists())
                .andExpect(jsonPath("commentId").exists());


    }

    @Test
    public void 댓글달기_댓글존재안함(){
        UploadedImage uploadedImage = UploadedImage.builder()
                .imageExtension("png")
                .imageName(UUID.randomUUID().toString())
                .imagePath("asdafasfasfasfd")
                .originalName("originalName")
                .account(createTestAccount())
                .build();

        Post post = Post.builder()
                .postText("Hello Im Post blabla")
                .account(createTestAccount())
                .uploadedImage(uploadedImage)
                .build();

        postRepository.save(post);


    }

}