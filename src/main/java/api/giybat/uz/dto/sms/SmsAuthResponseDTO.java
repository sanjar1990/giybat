package api.giybat.uz.dto.sms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmsAuthResponseDTO {
    private String message;
    private Data data;
    @Getter
    @Setter
    @ToString
    public static class Data{
        private String token;
    }
}

