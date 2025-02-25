package api.giybat.uz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachDTO {
    private String id;
    private String path;
    private String extension;
    private String origenName;
    private Long size;
    private LocalDateTime createdDate;
    private Boolean visible;
    private String url;
}
