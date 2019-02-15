package com.example.junsta.uploadImages;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedImageRepository extends JpaRepository<UploadedImage,Long> {

}
