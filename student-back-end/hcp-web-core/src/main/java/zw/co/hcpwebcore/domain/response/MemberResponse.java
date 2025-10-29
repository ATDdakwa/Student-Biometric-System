package zw.co.hcpwebcore.domain.response;

import zw.co.hcpwebcommons.domain.enums.Gender;
import zw.co.hcpwebcommons.domain.enums.MaritalStatus;
import zw.co.hcpwebcommons.domain.enums.Title;
import zw.co.hcpwebcommons.domain.value.Email;

import java.time.LocalDate;

public record MemberResponse(String reference, String firstName, String lastName, Gender gender,
                             LocalDate dateOfBirth,String nationalId, Title title, MaritalStatus maritalStatus, String msisdn,
                             Email email, String policyNumber, String policyType) {

}

