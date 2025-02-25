package api.giybat.uz.entity;

import api.giybat.uz.enums.EmailType;
import api.giybat.uz.enums.SmsType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "email_history")
public class EmailHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "email")
    private String email;
    @Column(name = "message",columnDefinition = "text")
    private String message;
    @Column(name = "code")
    private String code;
    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();
    @Column(name = "email_type")
    @Enumerated(EnumType.STRING)
    private EmailType emailType;
    @Column(name = "attempt_count")
    private Integer attemptCount=0;
}
