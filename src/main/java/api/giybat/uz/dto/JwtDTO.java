package api.giybat.uz.dto;

import api.giybat.uz.enums.ProfileRole;
import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtDTO {
    private String id;
    private String username;
    private List<ProfileRole> roleList;
}
