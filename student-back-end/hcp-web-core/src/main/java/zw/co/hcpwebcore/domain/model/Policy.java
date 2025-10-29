package zw.co.hcpwebcore.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import zw.co.hcpwebcommons.domain.enums.PolicyStatus;
import zw.co.hcpwebcommons.domain.enums.PolicyType;
import zw.co.hcpwebcommons.domain.value.AbstractAuditingEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "policies", schema = "hcp_ussd_dev")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EntityListeners(AuditingEntityListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Policy extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String policyHolder;

    @Column(unique = true)
    private String policyNumber;

    @Enumerated(EnumType.STRING)
    private PolicyType policyType;

    @Enumerated(EnumType.STRING)
    private PolicyStatus policyStatus;

    private int numberOfAdults;

    private int numberOfChildren;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Member member;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Beneficiary> beneficiaries;

}
