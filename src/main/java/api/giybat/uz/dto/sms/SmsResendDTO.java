package api.giybat.uz.dto.sms;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsResendDTO {
    @NotBlank
    private String phone;
}
