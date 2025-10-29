package zw.co.hcpwebcore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zw.co.hcpwebcommons.domain.enums.BeneficiaryCategory;
import zw.co.hcpwebcommons.domain.enums.ExceptionCode;
import zw.co.hcpwebcommons.domain.enums.PolicyStatus;
import zw.co.hcpwebcommons.exceptions.*;
import zw.co.hcpwebcore.domain.repository.BeneficiaryRepository;
import zw.co.hcpwebcore.domain.repository.MemberRepository;
import zw.co.hcpwebcore.domain.repository.PolicyRepository;
import zw.co.hcpwebcore.domain.response.PoliciesResponse;
import zw.co.hcpwebcore.domain.response.PolicyResponse;
import zw.co.hcpwebcore.domain.model.Policy;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final MemberRepository memberRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    public PolicyResponse addPolicy(String policyNumber) {

        var existingPolicy = policyRepository.findPolicyByPolicyNumber(policyNumber);

        if (existingPolicy.isPresent()) throw new PolicyNumberAlreadyExistsException("Policy already exits.", ExceptionCode.POLICY_NUMBER_EXISTS);

        var member = memberRepository.findByPolicyNumber(policyNumber);

        if (member.isEmpty()) {
            throw new MemberNotFoundException("Member with policy number: " + policyNumber + " not found.", ExceptionCode.MEMBER_NOT_FOUND);
        }
        var beneficiaries = beneficiaryRepository.findAllByPolicyNumber(policyNumber);

        //get total number of adults
        var adults = beneficiaries.stream()
                .filter(a -> a.getBeneficiaryCategory() == BeneficiaryCategory.ADULT)
                .toList();

        //get total number of kids
        var children = beneficiaries.stream()
                .filter(a -> a.getBeneficiaryCategory() == BeneficiaryCategory.CHILD)
                .toList();

        var policy = Policy.builder()
                .policyHolder(member.get().getFirstName().concat(" ").concat(member.get().getLastName()))
                .policyNumber(member.get().getPolicyNumber())
                .policyType(member.get().getPolicyType())
                .beneficiaries(beneficiaries)
                .member(member.get())
                .numberOfAdults(adults.size())
                .numberOfChildren(children.size())
                .build();

        if (member.get().isActive()) {
            policy.setPolicyStatus(PolicyStatus.ACTIVE);
        } else
            policy.setPolicyStatus(PolicyStatus.LAPSED);


        policyRepository.save(policy);

        return new PolicyResponse(policy.getPolicyHolder(), policy.getPolicyNumber(), policy.getPolicyType(), policy.getPolicyStatus(),
                policy.getNumberOfAdults(), policy.getNumberOfChildren(), policy.getBeneficiaries());

    }

    public PolicyResponse updatePolicyStatus(String policyNumber) {

        var policy = policyRepository.findPolicyByPolicyNumber(policyNumber);

        if (policy.isEmpty()) {
            throw new PolicyNotFoundException("Policy with policy number: " + policyNumber + " not found.", ExceptionCode.POLICY_NOT_FOUND);
        }

        var member = memberRepository.findByPolicyNumber(policyNumber);

        if (member.isEmpty()) {
            throw new MemberNotFoundException("Member with policy number: " + policyNumber + " not found.", ExceptionCode.MEMBER_NOT_FOUND);
        }


        if (member.get().isActive()) {
            policy.get().setPolicyStatus(PolicyStatus.ACTIVE);
        } else {
            policy.get().setPolicyStatus(PolicyStatus.LAPSED);
        }
        policyRepository.save(policy.get());

        return new PolicyResponse(policy.get().getPolicyHolder(), policy.get().getPolicyNumber(), policy.get().getPolicyType(), policy.get().getPolicyStatus(),
                policy.get().getNumberOfAdults(), policy.get().getNumberOfChildren(), policy.get().getBeneficiaries());

    }


    public PolicyResponse updatePolicyDependents(String policyNumber) {

        var policy = policyRepository.findPolicyByPolicyNumber(policyNumber);

        if (policy.isEmpty()) {
            throw new PolicyNotFoundException("Policy with policy number: " + policyNumber + " not found.", ExceptionCode.POLICY_NOT_FOUND);
        }

        var beneficiaries = beneficiaryRepository.findAllByPolicyNumber(policyNumber);

        //get total number of adults
        var adults = beneficiaries.stream()
                .filter(a -> a.getBeneficiaryCategory() == BeneficiaryCategory.ADULT)
                .toList();

        //get total number of kids
        var children = beneficiaries.stream()
                .filter(a -> a.getBeneficiaryCategory() == BeneficiaryCategory.CHILD)
                .toList();

        policy.get().setBeneficiaries(beneficiaries);

        policy.get().setNumberOfAdults(adults.size());

        policy.get().setNumberOfAdults(adults.size());


        policyRepository.save(policy.get());

        return new PolicyResponse(policy.get().getPolicyHolder(), policy.get().getPolicyNumber(), policy.get().getPolicyType(), policy.get().getPolicyStatus(),
                policy.get().getNumberOfAdults(), policy.get().getNumberOfChildren(), policy.get().getBeneficiaries());

    }


    public PolicyResponse getPolicy(String policyNumber) {
        var policy = policyRepository.findPolicyByPolicyNumber(policyNumber);

        if (policy.isEmpty()) throw new PolicyNotFoundException("Policy not found", ExceptionCode.POLICY_NOT_FOUND);


        return new PolicyResponse(policy.get().getPolicyHolder(), policy.get().getPolicyNumber(), policy.get().getPolicyType(), policy.get().getPolicyStatus(),
                policy.get().getNumberOfAdults(), policy.get().getNumberOfChildren(), policy.get().getBeneficiaries());
    }

    public List<PoliciesResponse> getAllPolicies() {
        var policies = policyRepository.findAll();

        if (policies.isEmpty())
            throw new PoliciesUnavailableException("No policies available at the moment.", ExceptionCode.POLICIES_UNAVAILABLE);

        return policies.stream().map(a -> new PoliciesResponse(a.getPolicyHolder(), a.getPolicyNumber(), a.getPolicyType(),
                a.getPolicyStatus(), a.getNumberOfAdults(), a.getNumberOfChildren())).collect(Collectors.toList());
    }


}
