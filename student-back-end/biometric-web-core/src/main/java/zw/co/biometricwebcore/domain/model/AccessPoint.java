package zw.co.biometricwebcore.domain.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "access_points")
@Builder
public class AccessPoint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // unique code for the access point (e.g., LIB_G1_DOOR)

    @Column(nullable = false)
    private String name; // human-readable name

    private String location; // optional location description

    @Column(nullable = false)
    private Boolean active = true;
}
