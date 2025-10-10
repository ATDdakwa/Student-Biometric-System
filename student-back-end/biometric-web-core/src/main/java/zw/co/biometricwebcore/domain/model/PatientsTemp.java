package zw.co.biometricwebcore.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@Entity
@EntityScan
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patientsTemp")
@Builder
@ToString
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PatientsTemp {
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

    @Column(name = "oldage")
    private String oldage;

}
