package api.giybat.uz.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PostAdminFilterMapperDTO {
   private String postId;
   private String title;
   private String postAttach;
   private LocalDateTime createdDate;
   private String profileId;
    private String profileName;
    private String username;
    private String profileAttach;

}
