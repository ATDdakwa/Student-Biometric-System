package zw.co.firstmutual.userservice.domain.dto.user;


import lombok.Data;

@Data
public class UserDto {
    private String firstName;
    private String lastName;
    private String sbu;
    private String email;
    private String password;
    private String userName;
    private String role;
}
