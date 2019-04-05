package com.example.junsta.uploadImages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadedImageService {

    private final UploadedImageRepository uploadedImageRepository;

    public UploadedImage save(UploadedImageDto dto) {
        UploadedImage image = UploadedImage.builder()
                .imageExtension(dto.getImageExtension())
                .imageName(dto.getImageName())
                .imagePath(dto.getImagePath())
                .originalName(dto.getOriginalName())
                .build();

        return uploadedImageRepository.save(image);
    }

    public Optional<UploadedImage> findById(Long uploadedImageId) {
        return uploadedImageRepository.findById(uploadedImageId);
    }

    public Optional<UploadedImage> findByImageName(String uploadedImageName) {
        return uploadedImageRepository.findByImageName(uploadedImageName);
    }


    public void delete(Long id) {

        Optional<UploadedImage> uploadedImage = findById(id);
        if (uploadedImage.isPresent()) {
            uploadedImageRepository.delete(uploadedImage.get());
        }
    }
}
