package zw.co.hcpwebcommons.register;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AddressDto {
    private String street;
    private String suburb;
    private String city;
}