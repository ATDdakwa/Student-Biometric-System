package zw.co.biometricwebcore.domain.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "access_point_assignments", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
@Builder
public class AccessPointAssignment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // verifier/app user assigned to an access point

    @ManyToOne(optional = false)
    @JoinColumn(name = "access_point_id")
    private AccessPoint accessPoint;

    @Column(nullable = false)
    private Boolean active = true;
}
