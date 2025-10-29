package zw.co.firstmutual.userservice.domain.dto.user;

import zw.co.hcpwebcommons.domain.enums.MaritalStatus;
import zw.co.hcpwebcommons.domain.enums.Title;
import zw.co.hcpwebcommons.domain.value.Address;
import zw.co.hcpwebcommons.domain.value.Email;
import zw.co.hcpwebcommons.domain.value.UserName;

public record UpdateUserDto(String firstName, String lastName, Email email, UserName userName, String password) {

}
