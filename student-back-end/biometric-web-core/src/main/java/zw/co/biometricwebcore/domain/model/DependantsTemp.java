package zw.co.biometricwebcore.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@EntityScan
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dependantsTemp")
@Builder
@ToString
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DependantsTemp {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "personnel_number")
    private String personnelNumber;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "dob")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String dob;

    @Column(name = "gender")
    private String gender;

    @Column(name = "suffix")
    private Integer suffix;

    @Column(name = "relation")
    private String relation;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "age")
    @Transient // This field will not be stored in the database
    private Integer age;

}
