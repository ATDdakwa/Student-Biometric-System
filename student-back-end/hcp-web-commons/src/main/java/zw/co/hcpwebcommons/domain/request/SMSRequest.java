package zw.co.hcpwebcommons.domain.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * @author Jackson Tungamira <tungamirajackson@gmail.com>
 */

@Data
@JsonSerialize
public class SMSRequest {
    private String originator;
    private String messageText;
    private String messageReference;
    private String messageDate;
    private String destination;
}