package zw.co.hcpwebcommons.domain.response;

import zw.co.hcpwebcommons.domain.enums.Gender;
import zw.co.hcpwebcommons.domain.enums.MaritalStatus;
import zw.co.hcpwebcommons.domain.enums.Title;
import zw.co.hcpwebcommons.domain.value.Email;
import zw.co.hcpwebcommons.domain.value.IdNumber;
import zw.co.hcpwebcommons.domain.value.MobileNumber;
import zw.co.hcpwebcommons.domain.value.UserName;

import java.time.LocalDate;

public record UserResponse(String username, Email email) {
}