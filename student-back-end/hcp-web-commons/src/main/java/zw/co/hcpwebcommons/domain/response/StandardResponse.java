package zw.co.hcpwebcommons.domain.response;

import lombok.*;

/**
 * @author nmathela
 */

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class StandardResponse {
    private String nextNode;
    private String displayMessage;
    private String sessionId;
    private String msisdn;
    private String applicationId;
    private Boolean successIndicator;
    private String stage;

    public StandardResponse(String nextNode, String displayMessage, Boolean successIndicator, String stage) {
        this.nextNode = nextNode;
        this.displayMessage = displayMessage;
        this.successIndicator = successIndicator;
        this.stage = stage;
    }
}