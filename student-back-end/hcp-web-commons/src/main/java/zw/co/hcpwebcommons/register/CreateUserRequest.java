package zw.co.hcpwebcommons.register;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zw.co.hcpwebcommons.domain.value.Email;
import zw.co.hcpwebcommons.domain.value.UserName;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

//    private String firstName;
//
//    private String lastName;

    private UserName userName;

    private Email email;

    private String password;
}