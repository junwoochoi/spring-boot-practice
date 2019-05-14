package com.example.junsta.posts;

import com.example.junsta.accounts.Account;
import com.example.junsta.accounts.AccountRequestDto;
import com.example.junsta.accounts.AccountService;
import com.example.junsta.common.BaseControllerTest;
import com.example.junsta.common.S3ImageUploader;
import com.example.junsta.common.S3MockConfig;
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

import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
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

@Transactional
@Import(S3MockConfig.class)
public class PostControllerTest extends BaseControllerTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UploadedImageRepository uploadedImageRepository;

    @Autowired
    private S3ImageUploader s3ImageUploader;
    @Autowired
    private S3Mock s3Mock;

    @Before
    public void setup() {
        createTestAccount();
        s3Mock.stop();
        s3Mock.start();
    }

    @After
    public void stopMockS3() {
        s3Mock.stop();
    }

    @Test
    public void 포스트생성_성공() throws Exception {
        String accessToken = getAccessToken();

        UploadedImage image = UploadedImage.builder()
                .originalName("originalName")
                .imagePath("imagePath")
                .imageName(UUID.randomUUID().toString())
                .imageExtension("png")
                .account(accountService.findByEmail(appProperties.getTestEmail()).get())
                .build();

        UploadedImage savedImage = uploadedImageRepository.save(image);

        PostRequestDto dto = PostRequestDto.builder()
                .uploadedImageName(savedImage.getImageName())
                .postText("포스트내용입니다 블라블라")
                .build();

        mockMvc.perform(
                post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andDo(document("create-post",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 정보 헤더")
                        ),
                        requestFields(
                                fieldWithPath("uploadedImageName").description("S3에 삽입된 이미지의 Name값"),
                                fieldWithPath("postText").description("포스트 본문")
                        ),
                        responseFields(
                                fieldWithPath("id").description("포스트 아이디"),
                                fieldWithPath("imagePath").description("S3에 삽입된 이미지 경로"),
                                fieldWithPath("originalName").description("S3에 삽입된 이미지의 원본 파일명"),
                                fieldWithPath("imageName").description("S3에 삽입된 이미지의 현재 파일명"),
                                fieldWithPath("imageExtension").description("S3에 삽입된 이미지 확장자"),
                                fieldWithPath("postText").description("포스트 본문"),
                                fieldWithPath("createdBy").description("포스트 작성자 이메일"),
                                fieldWithPath("createdAt").description("포스트 작성 일시"),
                                fieldWithPath("modifiedAt").description("포스트 수정 일시"),
                                fieldWithPath("likeCount").description("좋아요 받은 수")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("imageName").exists())
                .andExpect(jsonPath("originalName").exists())
                .andExpect(jsonPath("imagePath").exists())
                .andExpect(jsonPath("imageExtension").exists())
                .andExpect(jsonPath("postText").exists())
                .andExpect(jsonPath("createdAt").exists())
                .andExpect(jsonPath("modifiedAt").exists())
                .andExpect(jsonPath("createdBy").exists())
                .andExpect(jsonPath("likeCount").exists())
        ;
    }

    @Test
    public void 포스트생성_권한없음_실패() throws Exception {
        UploadedImage image = UploadedImage.builder()
                .originalName("originalName")
                .imagePath("imagePath")
                .imageName(UUID.randomUUID().toString())
                .imageExtension("png")
                .account(accountService.findByEmail(appProperties.getTestEmail()).get())
                .build();

        UploadedImage savedImage = uploadedImageRepository.save(image);


        PostRequestDto dto = PostRequestDto.builder()
                .uploadedImageName(savedImage.getImageName())
                .postText("포스트내용입니다 블라블라")
                .build();

        mockMvc.perform(
                post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 포스트생성_빈값입력_실패() throws Exception {
        UploadedImage image = UploadedImage.builder()
                .originalName("originalName")
                .imagePath("imagePath")
                .imageName(UUID.randomUUID().toString())
                .imageExtension("png")
                .account(accountService.findByEmail(appProperties.getTestEmail()).get())
                .build();

        UploadedImage savedImage = uploadedImageRepository.save(image);


        PostRequestDto dto = PostRequestDto.builder()
                .uploadedImageName(savedImage.getImageName())
                .postText("")
                .build();

        mockMvc.perform(
                post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void 포스트_받아오기_성공() throws Exception {
        IntStream.range(0, 30).forEach(this::createPost);


        mockMvc.perform(get("/api/posts/all")
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .param("page", "1")
                .param("size", "10")
                .param("sort", "createdAt,desc"))
                .andDo(print())
                .andDo(document(
                        "get-posts",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 정보 헤더")
                        ),
                        requestParameters(
                                parameterWithName("page").description("요청할 페이지 넘버"),
                                parameterWithName("size").description("한 페이지 당 사이즈"),
                                parameterWithName("sort").description("정렬할 기준 필드, 오름차순 or 내림차순")
                        ),
                        responseFields(
                                fieldWithPath("content").description("받아온 포스트 데이터 정보"),
                                fieldWithPath("content[].id").description("포스트 아이디"),
                                fieldWithPath("content[].imagePath").description("S3에 삽입된 이미지 경로"),
                                fieldWithPath("content[].originalName").description("S3에 삽입된 이미지의 원본 파일명"),
                                fieldWithPath("content[].imageName").description("S3에 삽입된 이미지의 현재 파일명"),
                                fieldWithPath("content[].imageExtension").description("S3에 삽입된 이미지 확장자"),
                                fieldWithPath("content[].postText").description("포스트 본문"),
                                fieldWithPath("content[].createdBy").description("포스트 작성자 이메일"),
                                fieldWithPath("content[].createdAt").description("포스트 작성 일시"),
                                fieldWithPath("content[].modifiedAt").description("포스트 수정 일시"),
                                fieldWithPath("content[].likeCount").description("좋아요 받은 수"),
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
                .andExpect(jsonPath("pageable").exists());
    }

    @Test
    public void 게시글_수정_성공() throws Exception {
        String accessToken = getAccessToken();
        Account account = accountService.findByEmail(appProperties.getTestEmail()).get();
        Post post = Post.builder()
                .postText("asdfasfd")
                .uploadedImage(
                        UploadedImage.builder()
                                .imagePath("imagePath")
                                .originalName("originName")
                                .imageName(UUID.randomUUID().toString())
                                .imageExtension("imageExtension")
                                .account(account)
                                .build()
                )
                .account(
                        account
                )
                .build();
        Post savedPost = postRepository.save(post);

        PostUpdateRequestDto dto = new PostUpdateRequestDto();
        String postText = "수정된 내용입니다.";
        dto.setPostText(postText);
        dto.setId(savedPost.getId());

        mockMvc.perform(
                put("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andDo(document("update-post",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 정보 헤더")
                        ),
                        requestFields(
                                fieldWithPath("id").description("포스트 아이디"),
                                fieldWithPath("postText").description("포스트 본문")
                        ),
                        responseFields(
                                fieldWithPath("id").description("포스트 아이디"),
                                fieldWithPath("imagePath").description("S3에 삽입된 이미지 경로"),
                                fieldWithPath("originalName").description("S3에 삽입된 이미지의 원본 파일명"),
                                fieldWithPath("imageName").description("S3에 삽입된 이미지의 현재 파일명"),
                                fieldWithPath("imageExtension").description("S3에 삽입된 이미지 확장자"),
                                fieldWithPath("postText").description("포스트 본문"),
                                fieldWithPath("createdBy").description("포스트 작성자 이메일"),
                                fieldWithPath("createdAt").description("포스트 작성 일시"),
                                fieldWithPath("modifiedAt").description("포스트 수정 일시"),
                                fieldWithPath("likeCount").description("좋아요 받은 수")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("imageName").exists())
                .andExpect(jsonPath("originalName").exists())
                .andExpect(jsonPath("imagePath").exists())
                .andExpect(jsonPath("imageExtension").exists())
                .andExpect(jsonPath("postText").exists())
                .andExpect(jsonPath("createdAt").exists())
                .andExpect(jsonPath("modifiedAt").exists())
                .andExpect(jsonPath("createdBy").exists())
        ;

    }

    @Test
    public void 게시글_수정_실패_빈값입력() throws Exception {
        String accessToken = getAccessToken();
        Account account = accountService.findByEmail(appProperties.getTestEmail()).get();
        Post post = Post.builder()
                .postText("asdfasfd")
                .uploadedImage(
                        UploadedImage.builder()
                                .imagePath("imagePath")
                                .originalName("originName")
                                .imageName(UUID.randomUUID().toString())
                                .account(account)
                                .imageExtension("imageExtension")
                                .build()
                )
                .account(
                        account
                )
                .build();
        Post savedPost = postRepository.save(post);

        PostUpdateRequestDto dto = new PostUpdateRequestDto();
        String postText = "";
        dto.setPostText(postText);
        dto.setId(savedPost.getId());

        mockMvc.perform(
                put("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    @Test
    public void 게시글_수정_실패_잘못된아이디값() throws Exception {
        String accessToken = getAccessToken();
        Account account = accountService.findByEmail(appProperties.getTestEmail()).get();
        Post post = Post.builder()
                .postText("asdfasfd")
                .uploadedImage(
                        UploadedImage.builder()
                                .imagePath("imagePath")
                                .originalName("originName")
                                .imageName("imageName")
                                .imageExtension("imageExtension")
                                .account(account)
                                .build()
                )
                .account(
                        account
                )
                .build();
        Post savedPost = postRepository.save(post);

        PostUpdateRequestDto dto = new PostUpdateRequestDto();
        String postText = "";
        dto.setPostText(postText);
        dto.setId(Long.valueOf(-1));

        mockMvc.perform(
                put("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    @Test
    public void 게시글_수정_실패_다른유저의게시물수정하려함() throws Exception {
        AccountRequestDto tempAccount = AccountRequestDto.builder()
                .displayName("asdf")
                .email("cjw4690@gmail")
                .password("password")
                .build();
        Account savedAccount = accountService.save(tempAccount);

        Post post = Post.builder()
                .postText("asdfasfd")
                .uploadedImage(
                        UploadedImage.builder()
                                .imagePath("imagePath")
                                .originalName("originName")
                                .imageName(UUID.randomUUID().toString())
                                .account(savedAccount)
                                .imageExtension("imageExtension")
                                .build()
                )
                .account(savedAccount)
                .build();
        Post savedPost = postRepository.save(post);

        PostUpdateRequestDto dto = new PostUpdateRequestDto();
        String postText = "수정됐어열";
        dto.setPostText(postText);
        dto.setId(savedPost.getId());

        mockMvc.perform(
                put("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;

    }

    @Test
    public void 게시글_삭제_성공() throws Exception {
        String accessToken = getAccessToken();

        IntStream.range(0, 3).forEach(this::createPost);

        Post post = postRepository.findAll().get(0);
        PostDeleteRequestDto dto = new PostDeleteRequestDto(post.getId());

        mockMvc.perform(
                delete("/api/posts")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andDo(document("delete-post",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 정보")
                        ),
                        requestFields(
                                fieldWithPath("id").description("게시글 id")
                        )
                        ))
                .andExpect(status().isNoContent());

        assertThat(uploadedImageRepository.findByImageName(post.getUploadedImage().getImageName()).isPresent()).isFalse();
        assertThat(postRepository.findById(post.getId()).isPresent()).isFalse();
    }

    @Test
    public void 게시글_삭제_권한없음() throws Exception {
        IntStream.range(0, 3).forEach(this::createPost);

        Post post = postRepository.findAll().get(0);
        PostDeleteRequestDto dto = new PostDeleteRequestDto(post.getId());

        mockMvc.perform(
                delete("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
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


}