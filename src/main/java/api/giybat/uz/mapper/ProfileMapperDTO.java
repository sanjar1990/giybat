package api.giybat.uz.mapper;

import api.giybat.uz.entity.ProfileRolesEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class ProfileMapperDTO {
     private String id;
     private String name;
     private String username;
     private String status;
     private String attachId;
     private Timestamp createdDate;
     private String roles;
     private Long postCount;

     // Getters and Setters
}
