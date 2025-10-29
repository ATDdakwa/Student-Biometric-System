package zw.co.biometricwebcore.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zw.co.hcpwebcommons.domain.value.AbstractAuditingEntity;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentFingerPrint extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "personnel_number_plus_suffix")
    private String personnelNumberPlusSuffix;
    @Transient
    private int suffix;

    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String firstFingerPrint;
    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String firstFingerImage;
    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String secondFingerPrint;
    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String secondFingerImage;
    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String thirdFingerPrint;
    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String thirdFingerImage;

    private String enrolmentStatus;

    @Lob
    private byte[] firstFingerTemplate = new byte[2500];
    @Lob
    private byte[] secondFingerTemplate = new byte[2500];
    @Lob
    private byte[] thirdFingerTemplate = new byte[2500];

}