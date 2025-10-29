package zw.co.hcpwebcore.domain.model;

import lombok.Getter;
import lombok.Setter;

import zw.co.hcpwebcommons.domain.value.AbstractAuditingEntity;


import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Payment")
public class Payment extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String reference;

    @Column(name = "customer_number")
    private String customerNumber;

    private String channel;

    private double amount;

    private String status;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "policy_number")
    private String policyNumber;

    private String currency;
}
