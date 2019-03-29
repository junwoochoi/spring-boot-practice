package com.example.junsta.uploadImages;

import com.example.junsta.common.S3ImageUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class UploadImageController {

    @Autowired
    private S3ImageUploader s3ImageUploader;

    @Autowired
    private UploadedImageService uploadedImageService;

    @PostMapping
    public ResponseEntity uploadImage(@RequestParam("data") MultipartFile file) throws IOException {
        if(!s3ImageUploader.validateType(file)){
            return ResponseEntity.badRequest().build();
        }
        UploadedImageDto dto = s3ImageUploader.upload(file, "static");

        UploadedImage savedImage = uploadedImageService.save(dto);

        return ResponseEntity.ok(new UploadedImageDto(savedImage));
    }

    @DeleteMapping
    public ResponseEntity deleteImage(String ImageName){
        return ResponseEntity.ok().build();
    }
}
