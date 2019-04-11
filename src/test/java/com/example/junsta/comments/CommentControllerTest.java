package com.example.junsta.comments;

import com.example.junsta.accounts.Account;
import com.example.junsta.accounts.AccountRequestDto;
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

import java.util.UUID;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.not;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        CommentPostRequestDto dto = CommentPostRequestDto.builder()
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
                                        fieldWithPath("postId").description("댓글 달린 게시글 ID"),
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
        CommentPostRequestDto dto = CommentPostRequestDto.builder()
                .postId(Long.valueOf(114634623))
                .commentText("this is comment contents")
                .build();

        mockMvc.perform(
                post("/api/comments")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
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

        CommentPostRequestDto dto = CommentPostRequestDto.builder()
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

        CommentPostRequestDto dto = CommentPostRequestDto.builder()
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
        Post post = createPost(11);

        IntStream.range(1, 10).forEach(value -> createComment(value, post));

        mockMvc.perform(
                get("/api/comments")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt,desc")
                        .param("postId", String.valueOf(post.getId()))
        )
                .andDo(print())
                .andDo(document(
                        "get-comments",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 정보 헤더")
                        ),
                        requestParameters(
                                parameterWithName("postId").description("댓글 조회 요청할 게시글 id"),
                                parameterWithName("page").description("요청할 페이지 넘버"),
                                parameterWithName("size").description("한 페이지 당 사이즈"),
                                parameterWithName("sort").description("정렬할 기준 필드, 오름차순 or 내림차순")
                        ),
                        responseFields(
                                fieldWithPath("content").description("받아온 포스트 데이터 정보"),
                                fieldWithPath("content[].id").description("댓글 id"),
                                fieldWithPath("content[].postId").description("댓글이 달린 게시글 id"),
                                fieldWithPath("content[].commentText").description("댓글 내용"),
                                fieldWithPath("content[].createdBy").description("댓글 작성자"),
                                fieldWithPath("content[].createdAt").description("댓글 작성 일시"),
                                fieldWithPath("content[].modifiedAt").description("댓글 수정 일시"),
                                fieldWithPath("pageable").description("페이징 관련 정보"),
                                fieldWithPath("pageable.sort").description("페이징 내 정렬 관련 정보"),
                                fieldWithPath("pageable.sort.sorted").description("페이지 정렬 여부"),
                                fieldWithPath("pageable.sort.unsorted").description("페이징 정렬이 되지않았는지 여부"),
                                fieldWithPath("pageable.sort.empty").description("페이지 정렬 비어있는지 여부"),
                                fieldWithPath("pageable.offset").description("페이징 Offset 정보"),
                                fieldWithPath("pageable.pageSize").description("페이지 사이즈 정보"),
                                fieldWithPath("pageable.pageNumber").description("페이지 넘버( 0부터 시작 ) 정보"),
                                fieldWithPath("pageable.paged").description("페이징 여부"),
                                fieldWithPath("pageable.unpaged").description("페이징 여부"),
                                fieldWithPath("totalPages").description("토탈 페이지 수"),
                                fieldWithPath("last").description("마지막 페이지인지 확인"),
                                fieldWithPath("totalElements").description("전체 게시글 수"),
                                fieldWithPath("number").description("페이지 넘버"),
                                fieldWithPath("size").description("페이지 나누는 사이즈"),
                                fieldWithPath("first").description("첫째 페이지인지 여부"),
                                fieldWithPath("numberOfElements").description("Elements 갯수"),
                                fieldWithPath("first").description("첫째 페이지인지 여부"),
                                fieldWithPath("sort").description("정렬 관련 정보"),
                                fieldWithPath("sort.sorted").description(" 정렬 여부"),
                                fieldWithPath("sort.unsorted").description(" 정렬이 되지않았는지 여부"),
                                fieldWithPath("sort.empty").description(" 비어있는지 여부"),
                                fieldWithPath("empty").description("비어있는지 여부")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").exists())
                .andExpect(jsonPath("content[0].postId").exists())
                .andExpect(jsonPath("content[0].id").exists())
                .andExpect(jsonPath("content[0].createdBy").exists())
                .andExpect(jsonPath("content[0].createdAt").exists())
                .andExpect(jsonPath("content[0].modifiedAt").exists())
                .andExpect(jsonPath("pageable").exists());
    }


    @Test
    public void 댓글_조회_실패_페이지값_유효성검사() throws Exception {
        mockMvc.perform(
                get("/api/comments")
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
                get("/api/comments")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "createdAt,desc")
                        .param("postId", "123141234123")
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void 댓글수정_성공() throws Exception {
        Post post = createPost(1);
        Comment comment = createComment(1, post);

        String updatedText = "updatedText";
        CommentPutRequestDto dto = CommentPutRequestDto.builder()
                .commentId(comment.getId())
                .commentText(updatedText)
                .build();
        mockMvc.perform(
                put("/api/comments")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("commentText").exists())
                .andExpect(jsonPath("createdBy").exists())
                .andExpect(jsonPath("modifiedAt").exists())
                .andExpect(jsonPath("createdAt").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("commentText").value(updatedText));
    }

    @Test
    public void 댓글수정_실패_댓글존재안함() throws Exception {

        String updatedText = "updatedText";
        CommentPutRequestDto dto = CommentPutRequestDto.builder()
                .commentId(Long.valueOf(1132411))
                .commentText(updatedText)
                .build();
        mockMvc.perform(
                put("/api/comments")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 댓글수정_실패_작성자가아님() throws Exception {
        Post post = createPost(1);
        Account anotherAccount = accountService.save(AccountRequestDto.builder()
                .displayName("anotherOne")
                .email("helloWorld@test.com")
                .password("password")
                .build());
        Comment comment = commentRepository.save(
                Comment.builder()
                        .account(anotherAccount)
                        .post(post)
                        .commentText("im commentText")
                        .build()
        );

        String updatedText = "updatedText";
        CommentPutRequestDto dto = CommentPutRequestDto.builder()
                .commentId(comment.getId())
                .commentText(updatedText)
                .build();
        mockMvc.perform(
                put("/api/comments")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andExpect(status().isUnauthorized());
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