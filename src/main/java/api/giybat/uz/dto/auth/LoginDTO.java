package api.giybat.uz.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    @NotBlank(message = "username is required")
    @Size(min = 3,message = "password length should be more then 6")
    private String username;
    @NotBlank(message = "username is required")
    @Size(min = 6,message = "password length should be more then 6")
    private String password;
}
