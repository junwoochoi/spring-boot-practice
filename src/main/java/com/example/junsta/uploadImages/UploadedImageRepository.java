package com.example.junsta.uploadImages;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadedImageRepository extends JpaRepository<UploadedImage,Long> {
    Optional<UploadedImage> findByImageName(String imageName);
}
