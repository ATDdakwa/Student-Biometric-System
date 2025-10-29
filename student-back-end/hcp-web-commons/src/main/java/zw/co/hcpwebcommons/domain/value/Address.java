package zw.co.hcpwebcommons.domain.value;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@Builder
public class Address {

    private String street;

    private String suburb;

    private String city;

}