package zw.co.biometricwebcore.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zw.co.hcpwebcommons.domain.value.AbstractAuditingEntity;

import javax.persistence.*;
import java.util.Arrays;


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
    @Transient
    private int suffix;
    private String firstFingerPrint;
    private String firstFingerImage;

    private String secondFingerPrint;
    private String secondFingerImage;

    private String thirdFingerPrint;
    private String thirdFingerImage;

    private String enrolmentStatus;

    @Lob
    private byte[] firstFingerTemplate = new byte[2500];
    @Lob
    private byte[] secondFingerTemplate = new byte[2500];
    @Lob
    private byte[] thirdFingerTemplate = new byte[2500];
}