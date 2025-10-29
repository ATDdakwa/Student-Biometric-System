package zw.co.hcpwebcore.domain.response;

import zw.co.hcpwebcommons.domain.enums.Gender;
import zw.co.hcpwebcommons.domain.enums.MaritalStatus;
import zw.co.hcpwebcommons.domain.enums.Title;
import zw.co.hcpwebcommons.domain.value.Email;

public record ActiveMemberResponse(String policyNumber, String firstName, String lastName, Gender gender,
                                   Title title, MaritalStatus maritalStatus,
                                   Email email, boolean active){
}
