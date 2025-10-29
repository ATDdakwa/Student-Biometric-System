package zw.co.hcpwebcore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import zw.co.hcpwebcommons.domain.enums.ExceptionCode;
import zw.co.hcpwebcommons.domain.value.IdNumber;
import zw.co.hcpwebcommons.exceptions.BeneficiariesNotFoundException;
import zw.co.hcpwebcommons.exceptions.BeneficiaryNotFoundException;
import zw.co.hcpwebcommons.exceptions.MemberNotFoundException;
import zw.co.hcpwebcore.domain.repository.BeneficiaryRepository;
import zw.co.hcpwebcore.domain.response.BeneficiaryResponse;
import zw.co.hcpwebcore.domain.model.Beneficiary;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    private  final JdbcTemplate jdbcTemplate;

    public List<BeneficiaryResponse> findAllBeneficiaries() {

        var beneficiaries = beneficiaryRepository.findAll();

        if (beneficiaries.isEmpty())
            throw new BeneficiariesNotFoundException("Beneficiaries Unavailable", ExceptionCode.BENEFICIARIES_UNAVAILABLE);

        return beneficiaries.stream().map(beneficiary -> new BeneficiaryResponse(beneficiary.getFirstName(), beneficiary.getLastName(),
                beneficiary.getGender(), beneficiary.getDateOfBirth(),beneficiary.getNationalId().toString(), beneficiary.getMsisdn().toString(),
                beneficiary.getTitle(), beneficiary.getBeneficiaryType(),beneficiary.getBeneficiaryCategory(),beneficiary.getPolicyNumber(),beneficiary.getCreatedDate(),
                beneficiary.isActive())).collect(Collectors.toList());
    }


    public BeneficiaryResponse findBeneficiaryByNationalId(IdNumber idNumber) {

        var beneficiary = beneficiaryRepository.findByNationalId(idNumber);

        if (beneficiary.isEmpty())
            throw new BeneficiaryNotFoundException("Beneficiary with Mobile Number: " + idNumber + " not found.", ExceptionCode.BENEFICIARY_NOT_FOUND);

         return  new BeneficiaryResponse(beneficiary.get().getFirstName(), beneficiary.get().getLastName(), beneficiary.get().getGender(),
                beneficiary.get().getDateOfBirth(), beneficiary.get().getNationalId().toString(), beneficiary.get().getMsisdn().toString(),
                beneficiary.get().getTitle(), beneficiary.get().getBeneficiaryType(),beneficiary.get().getBeneficiaryCategory(),
                 beneficiary.get().getPolicyNumber(),beneficiary.get().getCreatedDate(),beneficiary.get().isActive());
    }

    public List<BeneficiaryResponse> findAllBeneficiariesByPolicyNumber(String policyNumber) {

        var beneficiaries = beneficiaryRepository.findAllByPolicyNumber(policyNumber);

        if (beneficiaries.isEmpty())
            throw new BeneficiariesNotFoundException("Beneficiaries Unavailable.", ExceptionCode.BENEFICIARIES_UNAVAILABLE);

        return beneficiaries.stream().map(beneficiary -> new BeneficiaryResponse(beneficiary.getFirstName(), beneficiary.getLastName(),
                beneficiary.getGender(), beneficiary.getDateOfBirth(),beneficiary.getNationalId().toString(), beneficiary.getMsisdn().toString(),
                beneficiary.getTitle(), beneficiary.getBeneficiaryType(),beneficiary.getBeneficiaryCategory(),beneficiary.getPolicyNumber(),beneficiary.getCreatedDate(),
                beneficiary.isActive())).collect(Collectors.toList());
    }


    public String deactivateBeneficiary(IdNumber idNumber) {

        var beneficiary = beneficiaryRepository.findByNationalId(idNumber);

        if (beneficiary.isEmpty())
            throw new MemberNotFoundException("Beneficiary does not exist.", ExceptionCode.BENEFICIARY_NOT_FOUND);

        if (beneficiary.get().isActive()) {
            //deactivate beneficiary and update
            beneficiary.get().setActive(false);

            beneficiaryRepository.save(beneficiary.get());

        }
        return "Policy deactivated successfully.";
    }


    public String activateBeneficiary(IdNumber idNumber) {

        var beneficiary = beneficiaryRepository.findByNationalId(idNumber);

        if (beneficiary.isEmpty())
            throw new MemberNotFoundException("Beneficiary does not exist.", ExceptionCode.BENEFICIARY_NOT_FOUND);

        if (!beneficiary.get().isActive()) {
            //activate beneficiary and update
            beneficiary.get().setActive(true);

            beneficiaryRepository.save(beneficiary.get());

        }

        return "Policy activated successfully.";
    }

    public List<BeneficiaryResponse> findALlActiveBeneficiaries(boolean isActive) {

        var beneficiaries = beneficiaryRepository.findAllByActive(isActive);

        if (beneficiaries.isEmpty())
            throw new BeneficiariesNotFoundException("Beneficiaries Unavailable.", ExceptionCode.BENEFICIARIES_UNAVAILABLE);

        return beneficiaries.stream().map(beneficiary -> new BeneficiaryResponse(beneficiary.getFirstName(), beneficiary.getLastName(),
                beneficiary.getGender(), beneficiary.getDateOfBirth(),beneficiary.getNationalId().toString(), beneficiary.getMsisdn().toString(),
                beneficiary.getTitle(), beneficiary.getBeneficiaryType(),beneficiary.getBeneficiaryCategory(),beneficiary.getPolicyNumber(),beneficiary.getCreatedDate(),
                beneficiary.isActive())).collect(Collectors.toList());
    }

    public List<BeneficiaryResponse> findAllBeneficiariesByMembership(long memberId) {
        String query = "SELECT b.Beneficiary_id FROM beneficiary_main_member b WHERE b.member_id = " +memberId;
        List<Long> beneficiaryIds = jdbcTemplate.queryForList(query, Long.class);
        List<Beneficiary> beneficiaries = beneficiaryRepository.findAllById(beneficiaryIds);

        System.out.println("new object "+beneficiaries);

        if (beneficiaries.isEmpty())
            throw new BeneficiariesNotFoundException("Beneficiaries Unavailable.", ExceptionCode.BENEFICIARIES_UNAVAILABLE);

        return beneficiaries.stream().map(beneficiary -> new BeneficiaryResponse(beneficiary.getFirstName(), beneficiary.getLastName(),
                beneficiary.getGender(), beneficiary.getDateOfBirth(),beneficiary.getNationalId().toString(), beneficiary.getMsisdn().toString(),
                beneficiary.getTitle(), beneficiary.getBeneficiaryType(),beneficiary.getBeneficiaryCategory(),beneficiary.getPolicyNumber(),beneficiary.getCreatedDate(),
                beneficiary.isActive())).collect(Collectors.toList());
    }
}
