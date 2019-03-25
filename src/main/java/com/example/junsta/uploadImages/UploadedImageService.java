package com.example.junsta.uploadImages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UploadedImageService {

    @Autowired
    private UploadedImageRepository uploadedImageRepository;

    public UploadedImage save(UploadedImageDto dto){
        UploadedImage image = UploadedImage.builder()
                .imageExtension(dto.getImageExtension())
                .imageName(dto.getImageName())
                .imagePath(dto.getImagePath())
                .originalName(dto.getOriginalName())
                .build();

        return uploadedImageRepository.save(image);
    }
}
