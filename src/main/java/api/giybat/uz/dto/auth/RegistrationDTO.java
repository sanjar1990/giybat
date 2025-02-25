package api.giybat.uz.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {
    @NotBlank(message = "name is required")
    @Size(min = 3,message = "length should be more than three")
    private String name;
    @NotBlank(message = "username is required")
    @Size(min = 3,message = "length should be more than three")
    private String username;
    @NotBlank(message = "password is required")
    @Size(min = 3,message = "length should be more than three")
    private String password;
}
