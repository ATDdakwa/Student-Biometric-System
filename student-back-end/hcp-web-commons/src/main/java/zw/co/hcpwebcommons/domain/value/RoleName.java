package zw.co.hcpwebcommons.domain.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RoleName implements GrantedAuthority {

    public static final String ADMIN = "ADMIN"; //Super User
    public static final String MOBILE = "MOBILE";
    public static final String USSD = "USSD";

    private String authority;
}