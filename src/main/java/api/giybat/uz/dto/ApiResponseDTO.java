package api.giybat.uz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO {
    private boolean isError;
    private String message;
    private Object data;

    public ApiResponseDTO(boolean isError, Object data) {
        this.isError = isError;
        this.data = data;
    }

    public ApiResponseDTO(boolean isError, String message) {
        this.isError = isError;
        this.message = message;
    }

}
