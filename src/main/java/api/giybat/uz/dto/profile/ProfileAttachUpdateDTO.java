package api.giybat.uz.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileAttachUpdateDTO {
    @NotBlank(message = "attach id is required")
    private String attachId;
}
