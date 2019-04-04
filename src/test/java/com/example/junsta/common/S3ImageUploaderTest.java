package com.example.junsta.common;

import com.amazonaws.services.s3.AmazonS3;
import com.example.junsta.uploadImages.UploadedImageDto;
import io.findify.s3mock.S3Mock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Import(S3MockConfig.class)
public class S3ImageUploaderTest {

    @Autowired
    S3ImageUploader s3ImageUploader;
    @Autowired
    S3Mock s3Mock;
    @Autowired
    AmazonS3 s3;
    @Autowired
    ResourceLoader resourceLoader;


    @Test
    public void test() throws IOException {
        String expected = "mock1.png";
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", expected,
                "image/png", "test data".getBytes());
        UploadedImageDto uploadedImageDto = s3ImageUploader.upload(mockMultipartFile, "static");


        assertThat(uploadedImageDto.getImageExtension()).isEqualTo("png");
        assertThat(uploadedImageDto.getOriginalName()).isEqualTo(expected);

    }


    @After
    public void shutdownMockS3(){
        s3Mock.stop();
    }
}