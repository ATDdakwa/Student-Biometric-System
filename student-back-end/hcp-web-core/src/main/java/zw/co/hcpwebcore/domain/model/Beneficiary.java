package zw.co.hcpwebcore.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import zw.co.hcpwebcommons.domain.converter.EmailConverter;
import zw.co.hcpwebcommons.domain.converter.IdNumberConverter;
import zw.co.hcpwebcommons.domain.converter.MobileNumberConverter;
import zw.co.hcpwebcommons.domain.enums.BeneficiaryCategory;
import zw.co.hcpwebcommons.domain.enums.BeneficiaryType;
import zw.co.hcpwebcommons.domain.enums.Gender;
import zw.co.hcpwebcommons.domain.enums.Title;
import zw.co.hcpwebcommons.domain.value.AbstractAuditingEntity;
import zw.co.hcpwebcommons.domain.value.Email;
import zw.co.hcpwebcommons.domain.value.IdNumber;
import zw.co.hcpwebcommons.domain.value.MobileNumber;


import javax.persistence.*;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Beneficiary extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Past(message = "Date must be in the past")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Title title;

    @Enumerated(EnumType.STRING)
    private BeneficiaryType beneficiaryType;

    @Enumerated(EnumType.STRING)
    private BeneficiaryCategory beneficiaryCategory;

    @Convert(converter = EmailConverter.class)
    private Email email;                        //same as member

    @Convert(converter = MobileNumberConverter.class)
    private MobileNumber msisdn;                        //same as member

    @Column(unique = true,length = 20)
    @Convert(converter = IdNumberConverter.class)
    private IdNumber nationalId;

    private String policyNumber;                //same as member

    private boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Member> member;
}