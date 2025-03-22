package api.giybat.uz.dto.profile;

import api.giybat.uz.enums.GeneralStatus;
import api.giybat.uz.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProfileFilterDTO {
    private String name;
    private LocalDate from;
    private LocalDate to;
    private Boolean visible;
    private ProfileRole role;
    private GeneralStatus status;

}
