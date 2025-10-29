package zw.co.hcpwebcore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import zw.co.hcpwebcommons.domain.enums.ExceptionCode;
import zw.co.hcpwebcommons.domain.enums.PolicyType;
import zw.co.hcpwebcommons.domain.value.IdNumber;
import zw.co.hcpwebcommons.domain.value.MobileNumber;
import zw.co.hcpwebcommons.exceptions.MemberNotFoundException;
import zw.co.hcpwebcommons.exceptions.MembersUnavailableException;
import zw.co.hcpwebcommons.exceptions.PolicyNumberAlreadyExistsException;
import zw.co.hcpwebcore.domain.repository.BeneficiaryRepository;
import zw.co.hcpwebcore.domain.repository.MemberRepository;
import zw.co.hcpwebcore.domain.response.ActiveMemberResponse;
import zw.co.hcpwebcore.domain.response.MemberResponse;
import zw.co.hcpwebcore.request.MemberDto;
import zw.co.hcpwebcore.request.UpdateMemberDto;
import zw.co.hcpwebcore.domain.model.Beneficiary;
import zw.co.hcpwebcore.domain.model.Member;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private  final JdbcTemplate jdbcTemplate;
    private final BeneficiaryRepository beneficiaryRepository;

    public Member findMemberByPolicyNumber(String policyNumber) {

        var member = memberRepository.findByPolicyNumber(policyNumber);

        if (member.isEmpty())
            throw new MemberNotFoundException("Member with Policy Number: " + policyNumber + " not found", ExceptionCode.MEMBER_NOT_FOUND);

        return  member.get();
    }

    public Member findMemberByMsisdn(MobileNumber mobileNumber) {

        var member = memberRepository.findByMsisdn(mobileNumber);

        if (member.isEmpty())
            throw new MemberNotFoundException("Member with Mobile Number: " + mobileNumber + " not found", ExceptionCode.MEMBER_NOT_FOUND);

        return member.get();
    }

    public MemberResponse findMemberNationalId(IdNumber idNumber) {

        var member = memberRepository.findByNationalId(idNumber);

        if (member.isEmpty())
            throw new MemberNotFoundException("Member with National Id: " + idNumber + " not found", ExceptionCode.MEMBER_NOT_FOUND);

        return new MemberResponse(member.get().getReference(),member.get().getFirstName(), member.get().getLastName(),
                member.get().getGender(),member.get().getDateOfBirth(), member.get().getNationalId().toString(),
                member.get().getTitle(), member.get().getMaritalStatus(),member.get().getMsisdn().toString(), member.get().getEmail(), member.get().getPolicyNumber(),
                member.get().getPolicyType().toString());

    }

    public List<MemberResponse> findALlMembers() {

        var members = memberRepository.findAll();
        System.out.println("members......................."+members);
        if (members.isEmpty())
            throw new MembersUnavailableException("Members unavailable ", ExceptionCode.MEMBERS_UNAVAILABLE);

        return members.stream()
                .map(member -> new MemberResponse(
                        member.getReference(),
                        member.getFirstName(),
                        member.getLastName(),
                        member.getGender(),
                        member.getDateOfBirth(),
                        member.getNationalId().toString(),
                        member.getTitle(),
                        member.getMaritalStatus(),
                        member.getMsisdn().toString(),
                        member.getEmail(),
                        member.getPolicyNumber(),
                        member.getPolicyType() != null ? member.getPolicyType().toString() : null
                ))
                .collect(Collectors.toList());
    }


    //only add Policy number & activate user
    public String updateMember(UpdateMemberDto updateMemberDto){

        var member = findMemberByMsisdn(updateMemberDto.mobileNumber());

        if (member==null) throw new MemberNotFoundException("Member does not exist.",ExceptionCode.MEMBER_NOT_FOUND);

        if (member.getPolicyNumber()!=null) throw new PolicyNumberAlreadyExistsException("Member already has policy number", ExceptionCode.POLICY_NUMBER_EXISTS);
        //set policy number here
        member.setPolicyNumber(updateMemberDto.policyNumber());

        //member set to active once policy number is set
        member.setActive(true);

        memberRepository.save(member);

        return "Member details updated successfully.";
    }

    public String deactivatePolicy(String policyNumber ){

        var member = findMemberByPolicyNumber(policyNumber);

        if (member==null) throw new MemberNotFoundException("Member does not exist.",ExceptionCode.MEMBER_NOT_FOUND);

        if (member.isActive()) {
            //deactivate member and update
            member.setActive(false);

            memberRepository.save(member);

        }
        return "Policy deactivated successfully.";
    }

    public String activatePolicy(String policyNumber ) {

        var member = findMemberByPolicyNumber(policyNumber);

        if (member == null) throw new MemberNotFoundException("Member does not exist.", ExceptionCode.MEMBER_NOT_FOUND);

        if (!member.isActive()) {
            //deactivate member and update
            member.setActive(true);

            memberRepository.save(member);
        }
            return "Policy activated successfully.";
        }



    public List<ActiveMemberResponse> findALlActiveMembers(boolean active) {

        var members = memberRepository.findAllByActive(active);
        System.out.println("members..."+members);
        if (members.isEmpty())
            throw new MembersUnavailableException("Members unavailable ", ExceptionCode.MEMBERS_UNAVAILABLE);

        return members.stream().map(member -> new ActiveMemberResponse(member.getPolicyNumber(),member.getFirstName(), member.getLastName(), member.getGender(),
                member.getTitle(), member.getMaritalStatus(), member.getEmail(), member.isActive())).collect(Collectors.toList());
    }


    public List<Member> findAllMembers() {
        var members = memberRepository.findAll();

        return members;
    }

    public String updatePolicyNumber(MemberDto memberDto) {
        Long memberId = memberDto.getMemberId();
        List<Beneficiary> beneficiaries = getBeneficiariesByMemberId(memberId);
        Optional<Member> optionalMember = getMemberById(memberId);

        if (!beneficiaries.isEmpty()) {
            updateBeneficiariesPolicyNumber(beneficiaries, memberDto.getPolicyNumber());
        }

        if (optionalMember.isPresent()) {
            Member existingMember = optionalMember.get();
            updateMemberPolicy(existingMember, memberDto.getPolicyNumber());
            return "Policy Successfully Updated";
        } else {
            return "Member not found";
        }
    }

    private List<Beneficiary> getBeneficiariesByMemberId(Long memberId) {
        String query = "SELECT b.Beneficiary_id FROM beneficiary_main_member b WHERE b.member_id = " + memberId;
        List<Long> beneficiaryIds = jdbcTemplate.queryForList(query, Long.class);
        return beneficiaryRepository.findAllById(beneficiaryIds);
    }

    private Optional<Member> getMemberById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    private void updateBeneficiariesPolicyNumber(List<Beneficiary> beneficiaries, String policyNumber) {
        for (Beneficiary beneficiary : beneficiaries) {
            beneficiary.setPolicyNumber(policyNumber);
        }
        beneficiaryRepository.saveAll(beneficiaries);
    }

    private void updateMemberPolicy(Member member, String policyNumber) {
        member.setPolicyNumber(policyNumber);
        member.setPolicyType(PolicyType.HCP_USD);
        memberRepository.save(member);
    }
}
