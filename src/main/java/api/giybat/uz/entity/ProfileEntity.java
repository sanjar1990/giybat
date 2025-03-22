package api.giybat.uz.entity;

import api.giybat.uz.enums.GeneralStatus;
import java.util.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileEntity {
    @Id
   @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "username")
    private String username;
    @Column(name = "temp_username")
    private String tempUsername;
    @Column
    private String password;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GeneralStatus status;
    @Column(name = "attach_id")
    private String attachId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id",updatable = false,insertable = false)
    private AttachEntity attach;
    @Column
    private Boolean visible=Boolean.TRUE;
    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();
    @OneToMany(mappedBy = "profile")
    List<ProfileRolesEntity> roleList;
}
