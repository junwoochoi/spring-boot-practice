package com.example.junsta.uploadImages;


import com.example.junsta.common.BaseControllerTest;
import com.example.junsta.common.S3MockConfig;
import io.findify.s3mock.S3Mock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(S3MockConfig.class)
public class UploadImageControllerTest extends BaseControllerTest {

    @Autowired
    S3Mock s3Mock;

    @Before
    public void startMockS3() {
        s3Mock.stop();
        s3Mock.start();
    }

    @Test
    public void 이미지업로드_성공() throws Exception {
        String expected = "test.png";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("data", expected,
                "image/png", "test data".getBytes());
        mockMvc.perform(
                multipart("/api/images")
                        .file(mockMultipartFile)
                        .header(HttpHeaders.AUTHORIZATION, getAccessToken())
        )
                .andDo(print())
                .andDo(document("post-image",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("헤더 인증 정보"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입")
                        ),
                        requestPartBody("data"),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("컨텐츠 타입")
                        ),
                        responseFields(
                                fieldWithPath("originalName").description("이미지의 원본 파일명"),
                                fieldWithPath("imageName").description("이미지의 현재 파일명 ( UUID )"),
                                fieldWithPath("imageExtension").description("이미지의 확장자명"),
                                fieldWithPath("imagePath").description("이미지의 현재 주소")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("originalName").exists())
                .andExpect(jsonPath("imageName").exists())
                .andExpect(jsonPath("imageExtension").exists())
                .andExpect(jsonPath("imagePath").exists())
        ;
    }

    @After
    public void stopMockS3() {
        s3Mock.stop();
    }

}