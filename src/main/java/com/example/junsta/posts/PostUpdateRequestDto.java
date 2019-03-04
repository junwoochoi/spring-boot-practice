package com.example.junsta.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class PostUpdateRequestDto {
    @Min(0)
    private Long id;

    @NotEmpty @NotBlank(message = "postText should not be blank")
    private String postText;
}
