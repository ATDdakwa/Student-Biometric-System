package zw.co.hcpwebcore.domain.response;

import zw.co.hcpwebcommons.domain.enums.BeneficiaryCategory;
import zw.co.hcpwebcommons.domain.enums.BeneficiaryType;
import zw.co.hcpwebcommons.domain.enums.Gender;
import zw.co.hcpwebcommons.domain.enums.Title;

import java.time.Instant;
import java.time.LocalDate;

public record BeneficiaryResponse(String firstName, String lastName,
                                  Gender gender, LocalDate dateOfBirth, String nationalId, String msisdn, Title title,
                                  BeneficiaryType relationship, BeneficiaryCategory beneficiaryCategory, String policyNumber,Instant createdDate,
                                  boolean isActive ) {
}