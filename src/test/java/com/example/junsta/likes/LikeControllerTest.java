package com.example.junsta.likes;

import com.example.junsta.accounts.Account;
import com.example.junsta.common.BaseControllerTest;
import com.example.junsta.common.S3ImageUploader;
import com.example.junsta.common.S3MockConfig;
import com.example.junsta.posts.Post;
import com.example.junsta.posts.PostRepository;
import com.example.junsta.uploadImages.UploadedImage;
import com.example.junsta.uploadImages.UploadedImageDto;
import com.example.junsta.uploadImages.UploadedImageRepository;
import io.findify.s3mock.S3Mock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@Import(S3MockConfig.class)
public class LikeControllerTest extends BaseControllerTest {

    @Autowired
    S3ImageUploader s3ImageUploader;
    @Autowired
    S3Mock s3Mock;
    @Autowired
    UploadedImageRepository uploadedImageRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    LikeService likeService;

    @Before
    public void setup() {
        createTestAccount();
        s3Mock.stop();
        s3Mock.start();
    }

    @After
    public void after() {
        s3Mock.stop();
    }

    @Test
    public void 좋아요_성공() throws Exception {
        createTestAccount();
        Post post = createPost(1);
        LikeDto dto = LikeDto.builder().postId(post.getId()).build();

        mockMvc.perform(
                post("/api/likes")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andDo(document("create-like",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 정보"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("미디어 타입")
                        ),
                        requestFields(
                                fieldWithPath("postId").description("좋아요하려는 포스트")
                        )
                        )
                )
                .andExpect(status().isNoContent());

        assertThat(post.getLikeUsers().size()).isEqualTo(1);

    }

    @Test
    public void 좋아요_존재하지않는포스트() throws Exception {
        createTestAccount();

        LikeDto dto = LikeDto.builder().postId((long) 1251253).build();

        mockMvc.perform(
                post("/api/likes")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());


    }

    @Test
    public void 좋아요취소_성공() throws Exception {
        Account account = createTestAccount();
        Post post = createPost(1);
        likeService.createLike(post.getId(), account);
        LikeDto dto = LikeDto.builder().postId(post.getId()).build();

        mockMvc.perform(
                delete("/api/likes")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andDo(document("cancel-like",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 정보"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("미디어 타입")
                        ),
                        requestFields(
                                fieldWithPath("postId").description("좋아요 취소하려는 포스트")
                        )
                        )
                )
                .andExpect(status().isNoContent());

        assertThat(post.getLikeUsers().size()).isEqualTo(0);

    }

    @Test
    public void 좋아요취소_좋아요를_하지않아도_성공() throws Exception {
        Account account = createTestAccount();
        Post post = createPost(1);
        LikeDto dto = LikeDto.builder().postId(post.getId()).build();

        mockMvc.perform(
                delete("/api/likes")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThat(post.getLikeUsers().size()).isEqualTo(0);

    }


    private Post createPost(int i) {
        Post post = null;
        try {

            Account account = accountService.findByEmail(appProperties.getTestEmail()).get();
            MockMultipartFile file = new MockMultipartFile("data", "test.png", "/image/png", "testtest".getBytes());
            UploadedImageDto uploadedImageDto = null;

            uploadedImageDto = s3ImageUploader.upload(file, "static");
            UploadedImage uploadedImage = UploadedImage.builder()
                    .imageExtension(uploadedImageDto.getImageExtension())
                    .imagePath(uploadedImageDto.getImagePath())
                    .account(account)
                    .imageName(uploadedImageDto.getImageName())
                    .originalName(uploadedImageDto.getOriginalName())
                    .build();

            UploadedImage savedUploadImage = uploadedImageRepository.save(uploadedImage);

            post = Post.builder()
                    .uploadedImage(savedUploadImage)
                    .postText("post" + i)
                    .account(account)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return postRepository.save(post);
    }
}