package com.example.junsta.posts;

import com.example.junsta.accounts.Account;
import com.example.junsta.exceptions.UnauthorizedException;
import com.example.junsta.exceptions.UploadedImageNotExistException;
import com.example.junsta.uploadImages.UploadedImage;
import com.example.junsta.uploadImages.UploadedImageService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {



    @NotNull @NotBlank(message = "uploadedImageName should not be null or blank")
    private String uploadedImageName;
    @NotEmpty @NotBlank(message = "postText should not be blank")
    private String postText;
    @JsonIgnore
    private Account account;


}
