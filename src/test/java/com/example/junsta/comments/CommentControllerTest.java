package com.example.junsta.comments;

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
import lombok.RequiredArgsConstructor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(S3MockConfig.class)
public class CommentControllerTest extends BaseControllerTest {

    @Autowired
    CommentService commentService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UploadedImageRepository uploadedImageRepository;
    @Autowired
    S3ImageUploader s3ImageUploader;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    S3Mock s3Mock;

    @Before
    public void setup() {
        s3Mock.stop();
        s3Mock.start();

        createTestAccount();
        Post post = createPost(1);
        IntStream.range(0, 10).forEach(i -> createComment(i, post));
    }

    @After
    public void done() {
        s3Mock.stop();
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
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andDo(
                        document("create-comment",
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("인증정보"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("콘텐츠 타입 정보")
                                ),
                                requestFields(
                                        fieldWithPath("postId").description("댓글 달 게시글 ID"),
                                        fieldWithPath("commentText").description("댓글 내용")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("댓글 ID"),
                                        fieldWithPath("commentText").description("댓글 내용"),
                                        fieldWithPath("createdBy").description("댓글 작성자"),
                                        fieldWithPath("createdAt").description("작성 일시"),
                                        fieldWithPath("modifiedAt").description("최근 수정 일시")
                                )
                        )

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("commentText").exists())
                .andExpect(jsonPath("createdBy").exists())
                .andExpect(jsonPath("modifiedAt").exists())
                .andExpect(jsonPath("createdAt").exists())
                .andExpect(jsonPath("id").exists());
    }

    @Test
    public void 댓글달기_게시글존재안함() throws Exception {
        CommentRequestDto dto = CommentRequestDto.builder()
                .postId(Long.valueOf(114634623))
                .commentText("this is comment contents")
                .build();

        mockMvc.perform(
                post("/api/comments")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void 댓글달기_입력값_유효성검사_빈내용입력() throws Exception {
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
                .commentText("")
                .build();

        mockMvc.perform(
                post("/api/comments")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void 댓글달기_입력값_유효성검사_id없음() throws Exception {
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
                .commentText("sadfasdf")
                .build();

        mockMvc.perform(
                post("/api/comments")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andExpect(status().isBadRequest());

    }


    @Test
    public void 댓글_조회_성공() throws Exception {
        mockMvc.perform(
                get("/api/posts")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "createdAt,desc")
                        .param("postId", "1")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").exists())
                .andExpect(jsonPath("pageable").exists());
    }


    @Test
    public void 댓글_조회_실패_페이지값_유효성검사() throws Exception {
        mockMvc.perform(
                get("/api/posts")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .param("page", "-100")
                        .param("size", "10")
                        .param("sort", "createdAt,desc")
                        .param("postId", "1")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 댓글_조회_실패_존재하지않는포스트() throws Exception {
        mockMvc.perform(
                get("/api/posts")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "createdAt,desc")
                        .param("postId", "123141234123")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
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


    private Comment createComment(int i, Post post) {

        return commentRepository.save(
                Comment.builder()
                        .account(createTestAccount())
                        .post(post)
                        .commentText("im commentText" + i)
                        .build()
        );
    }


}