package api.giybat.uz.dto.sms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsSendRequestDTO {
    private String mobile_phone;
    private String message;
}
