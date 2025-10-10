package zw.co.biometricwebcore.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityScan
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students")
@Builder
@ToString
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "student_number", unique = true)
    private String studentNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "dob")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String dob; // stored as string for compatibility with existing pattern

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "initials")
    private String initials;

    @Column(name = "id_number", unique = true)
    private String idNumber;

    @Column(name = "gender")
    private String gender;

    @Column(name = "status")
    private String status; // e.g., REGISTERED, SUSPENDED

    @Transient
    private Integer age;

    @PostLoad
    @PostPersist
    @PostUpdate
    public void calculateAge() {
        if (dob != null && !dob.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate birthDate = LocalDate.parse(dob, formatter);
                LocalDate currentDate = LocalDate.now();
                this.age = Period.between(birthDate, currentDate).getYears();
            } catch (Exception e) {
                this.age = null;
            }
        } else {
            this.age = null;
        }
    }

    @Column(name = "email")
    private String email;

    @Column(name = "faculty")
    private String faculty;

    @Column(name = "programme")
    private String programme;

    @Column(name = "is_biometric")
    private Boolean isBiometric = false;

    @Column(name = "enrolment_status")
    private String enrolmentStatus; // e.g., ENROLLED, PENDING

    @Column(name = "biometric_tag", columnDefinition = "LONGTEXT")
    private String biometricTag;
}
