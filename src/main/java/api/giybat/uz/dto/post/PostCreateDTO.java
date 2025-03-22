package api.giybat.uz.dto.post;

import api.giybat.uz.dto.AttachDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateDTO {
    @NotBlank(message = "title is required")
    @Size(min = 5,message = "More then 5 character")
    private String title;
    @NotBlank(message = "content is required")
    private String content;
    @NotBlank(message = "attach id is required")
    private String attachId;
}
