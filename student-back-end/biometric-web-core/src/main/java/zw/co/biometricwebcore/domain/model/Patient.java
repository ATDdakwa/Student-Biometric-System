package zw.co.biometricwebcore.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.LocalDate;
import java.time.Period;


import javax.persistence.*;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import zw.co.hcpwebcommons.domain.enums.Gender;
import zw.co.hcpwebcommons.domain.enums.MaritalStatus;

@Data
@Entity
@EntityScan
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
@Builder
@ToString
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "personnel_number")
    private String personnelNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "dob")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String dob;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "initials")
    private String initials;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "gender")
    private String gender;

    @Column(name = "status")
    private String status;

    @Column(name = "age")
    @Transient // This field will not be stored in the database
    private Integer age;
    @PostLoad // This method will be called after loading the entity from the database
    public void calculateAge() {
        if (dob != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate birthDate = LocalDate.parse(dob, formatter);
            LocalDate currentDate = LocalDate.now();
            this.age = Period.between(birthDate, currentDate).getYears();
        } else {
            this.age = null; // Set to null if dob is null
        }
    }


    @Column(name = "company")
    private String company;

    @Column(name = "department")
    private String department;

    @Column(name = "division")
    private String division;

    @Column(name = "section")
    private String section;

    @Column(name = "email")
    private String email;

    @Column(name = "scheme")
    private String scheme;

//    @Column(name = "relation")
//    private String relation;

    @Column(name = "oldage")
    private String oldage;

//    @Column(name = "suffix")
//    private Integer suffix;

//    @Column(name = "patient_type")
//    private String patientType;


    @Column(name = "isBiometric")
    private Boolean isBiometric = false;

    @Column(name = "enrolment_status")
    private String enrolmentStatus;

    @Column(name = "biometric_tag", columnDefinition = "LONGTEXT")
    private String biometric_tag;

    @Column(name = "coe_document_path")
    private String coeDocumentPath; // Path to the COE document
}