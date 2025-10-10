package zw.co.biometricwebcore.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zw.co.hcpwebcommons.domain.value.AbstractAuditingEntity;

import javax.persistence.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientFingerPrint extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "personnel_number_plus_suffix")
    private String personnelNumberPlusSuffix;

    private String firstFingerPrint;

    private String secondFingerPrint;

    private String thirdFingerPrint;

    private String enrolmentStatus;
}