package com.example.junsta.posts;

import com.example.junsta.accounts.Account;
import com.example.junsta.exceptions.UnauthorizedException;
import com.example.junsta.exceptions.UploadedImageNotExistException;
import com.example.junsta.uploadImages.UploadedImage;
import com.example.junsta.uploadImages.UploadedImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PostRequestValidator {

    private final UploadedImageService uploadedImageService;

    @Transactional
    public void validateUploadedImage(PostRequestDto dto, Account account){

        UploadedImage uploadedImage = uploadedImageService.findByImageName(dto.getUploadedImageName()).orElseThrow(UploadedImageNotExistException::new);

        if(!uploadedImage.getAccount().equals(account)){
            throw new UnauthorizedException();
        }
    }
}
