package zw.co.biometricwebcore.domain.model;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "sms_notifications")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SMSNotifications implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mobileNumber;
    private String message;

    @Enumerated(EnumType.STRING)
    private SMSStatus status; // Enum to represent status (SENT, FAILED, etc.)

    @Temporal(TemporalType.TIMESTAMP)
    private Date sentDate;
}