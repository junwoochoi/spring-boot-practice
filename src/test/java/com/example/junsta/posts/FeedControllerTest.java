package com.example.junsta.posts;

import akka.http.javadsl.Http;
import com.example.junsta.accounts.Account;
import com.example.junsta.accounts.AccountRepository;
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

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@Import(S3MockConfig.class)
public class FeedControllerTest extends BaseControllerTest {

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
    public void 팔로우기반_게시글_받아오기_성공() throws Exception {
        AccountRequestDto test = AccountRequestDto.builder()
                .displayName("hello")
                .email("hello@naver.com")
                .password("password")
                .build();

        Account friend = accountService.save(test);
        IntStream.range(0, 10).forEach(value -> createPost(value, friend));
        AccountRequestDto test2 = AccountRequestDto.builder()
                .displayName("hello")
                .email("hello2@naver.com")
                .password("password")
                .build();

        Account friend2 = accountService.save(test2);
        IntStream.range(0, 10).forEach(value -> createPost(value, friend2));

        Account testAccount = createTestAccount();
        testAccount.startFollow(friend);

        mockMvc.perform(
                get("/api/posts")
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8)
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "createdAt,desc")
        )
                .andDo(
                        print()
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("pageable").exists())
                .andExpect(jsonPath("totalElements").value("10"));

    }

    private Post createPost(int i, Account account) {
        Post post = null;

        try {
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
