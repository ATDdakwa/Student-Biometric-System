package zw.co.hcpwebcore.domain.response;

import zw.co.hcpwebcommons.domain.enums.PolicyStatus;
import zw.co.hcpwebcommons.domain.enums.PolicyType;

public record PoliciesResponse(String nameOfPolicyHolder, String policyNumber, PolicyType policyType,
                               PolicyStatus policyStatus, int numberOfAdults,
                               int numberOfChildren) {
}
