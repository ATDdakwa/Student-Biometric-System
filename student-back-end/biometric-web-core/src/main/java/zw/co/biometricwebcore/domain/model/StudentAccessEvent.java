package zw.co.biometricwebcore.domain.model;

import lombok.*;
import zw.co.hcpwebcommons.domain.value.AbstractAuditingEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student_access_events")
@Builder
public class StudentAccessEvent extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "access_point_id")
    private AccessPoint accessPoint;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String direction; // ENTRY or EXIT

    private Boolean verified; // biometric verification result

    private String reason; // optional reason (e.g., not registered)
}
