package com.example.junsta.posts;

import com.example.junsta.common.BaseControllerTest;
import com.example.junsta.uploadImages.UploadedImage;
import com.example.junsta.uploadImages.UploadedImageRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerTest extends BaseControllerTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UploadedImageRepository uploadedImageRepository;

    @Test
    public void 포스트생성_성공() throws Exception{
        PostRequestDto dto = PostRequestDto.builder()
                .imageExtension("png")
                .imageName("imageName")
                .imagePath("imagePath")
                .originalName("originalName")
                .postText("포스트내용입니다 블라블라")
                .build();

        mockMvc.perform(
                post("/api/post")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("imageName").exists())
        .andExpect(jsonPath("originalName").exists())
        .andExpect(jsonPath("imagePath").exists())
        .andExpect(jsonPath("imageExtension").exists())
        .andExpect(jsonPath("commentList").exists())
        .andExpect(jsonPath("postText").exists())
        .andExpect(jsonPath("createdAt").exists())
        .andExpect(jsonPath("modifiedAt").exists())
        .andExpect(jsonPath("createdBy").exists())
        ;
    }

    @Test
    public void 포스트생성_권한없음_실패() throws Exception{
        PostRequestDto dto = PostRequestDto.builder()
                .imageExtension("png")
                .imageName("imageName")
                .imagePath("imagePath")
                .originalName("originalName")
                .postText("포스트내용입니다 블라블라")
                .build();

        mockMvc.perform(
                post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 포스트생성_빈값입력_실패() throws Exception{
        PostRequestDto dto = PostRequestDto.builder()
                .imageExtension("png")
                .imageName("")
                .imagePath("")
                .originalName("originalName")
                .postText("포스트내용입니다 블라블라")
                .build();

        mockMvc.perform(
                post("/api/post")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(HttpHeaders.AUTHORIZATION, getAccessToken())
                .content(objectMapper.writeValueAsBytes(dto))
        )
                .andDo(print())
        .andExpect(status().isBadRequest());
    }

    @Test
    public void 포스트_받아오기_성공() throws Exception {

        Post post = createPost(0);

        mockMvc.perform(get("/api/post")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,desc"))
                .andDo(print())
                .andExpect(status().isOk()
                );
    }

    private Post createPost(int i) {
        UploadedImage uploadedImage = UploadedImage.builder()
                .imageExtension("imageExtension")
                .imageName("imageName")
                .originalName("originName")
                .build();

        UploadedImage savedUploadImage = uploadedImageRepository.save(uploadedImage);

        Post post = Post.builder()
                .uploadedImage(savedUploadImage)
                .postText("post"+i)
                .build();

        return postRepository.save(post);
    }

}