package api.giybat.uz.dto.profile;

import api.giybat.uz.dto.AttachDTO;
import api.giybat.uz.enums.GeneralStatus;
import api.giybat.uz.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private String id;
    private String name;
    private String username;
    private GeneralStatus status;
    private String jwt;
    private List<ProfileRole> roleList;
    private AttachDTO attach;
    private Long postCount;
    private LocalDateTime createdDate;

}
