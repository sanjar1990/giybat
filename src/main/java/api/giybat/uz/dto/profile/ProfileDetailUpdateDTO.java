package api.giybat.uz.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDetailUpdateDTO {
    @NotBlank(message = "name is required")
    private String name;
}
