package api.giybat.uz.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordConfirmDTO {
    @NotBlank(message = "is reaquired")
    private String username;
    @NotBlank(message = "is reaquired")
    private String password;
    @NotBlank(message = "is reaquired")
    private String code;
}
