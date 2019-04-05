package com.example.junsta.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.example.junsta.uploadImages.UploadedImage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3ImageRemover {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void deleteFromS3(UploadedImage image) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, "static/"+image.getImageName()+"."+image.getImageExtension()));
    }
}
