package api.giybat.uz.dto.post;

import api.giybat.uz.dto.AttachDTO;
import api.giybat.uz.dto.profile.ProfileDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {
    private String id;
    private String title;
    private String content;
    private AttachDTO attach;
    private LocalDateTime createdAt;
    private ProfileDTO profile;

}
