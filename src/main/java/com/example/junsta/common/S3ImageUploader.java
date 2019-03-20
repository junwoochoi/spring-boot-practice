package com.example.junsta.common;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageUploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile).orElseThrow(
                () -> new IllegalArgumentException("MultipartFile => File 변환에 실패했습니다.")
        );
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);
        return uploadUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File uploadFile) {
        if (uploadFile.delete()) {
            log.info("{} 파일이 삭제되었습니다.", uploadFile.getName());
            return;
        }
        log.info("{} 파일 삭제에 실패했습니다.", uploadFile.getName());
    }


    private Optional<File> convert(MultipartFile multipartFile) throws IOException {
        File convertedFile = new File(multipartFile.getOriginalFilename());

        if (convertedFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertedFile);
        }

        return Optional.empty();
    }
}
