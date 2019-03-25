package com.example.junsta.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.junsta.uploadImages.UploadedImage;
import com.example.junsta.uploadImages.UploadedImageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3ImageUploader {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public UploadedImageDto upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile).orElseThrow(
                () -> new IllegalArgumentException("MultipartFile => File 변환에 실패했습니다.")
        );
        return upload(uploadFile, dirName);
    }

    private UploadedImageDto upload(File uploadFile, String dirName) {
        String fileName = dirName
                + "/"
                + UUID.randomUUID().toString()
                +"."
                + FilenameUtils.getExtension(uploadFile.getName());
        String uploadUrl = putS3(uploadFile, fileName);

        UploadedImageDto dto = UploadedImageDto.builder()
                .originalName(uploadFile.getName())
                .imagePath(uploadUrl)
                .imageName(uploadUrl.substring(uploadUrl.lastIndexOf("/")))
                .imageExtension(FilenameUtils.getExtension(uploadFile.getName()))
                .build();

        removeNewFile(uploadFile);
        return dto;
    }

    private String putS3(File uploadFile, String fileName) {
        try {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3.getUrl(bucket, fileName).toString();
        } catch (Exception e) {
            throw new AmazonS3Exception(bucket, e);
        }
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

    public boolean validateType(MultipartFile file){
        String mimeType = file.getContentType();
        if(mimeType.contains("image/")){
            return true;
        }
        return false;
    }
}
