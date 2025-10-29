package zw.co.hcpwebcore.domain.response;

import zw.co.hcpwebcommons.domain.enums.PolicyStatus;
import zw.co.hcpwebcommons.domain.enums.PolicyType;
import zw.co.hcpwebcore.domain.model.Beneficiary;

import java.util.List;


public record PolicyResponse(String nameOfPolicyHolder, String policyNumber, PolicyType policyType, PolicyStatus policyStatus, int numberOfAdults,
                             int numberOfChildren, List<Beneficiary> beneficiaries) {
}
