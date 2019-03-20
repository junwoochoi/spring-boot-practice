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

    @PostMapping
    public ResponseEntity uploadImage(@RequestParam("data") MultipartFile file) throws IOException {
        String uploadResult = s3ImageUploader.upload(file, "static");
        return ResponseEntity.ok(uploadResult);
    }

    @DeleteMapping
    public ResponseEntity deleteImage(String ImageName){
        return ResponseEntity.ok().build();
    }
}
