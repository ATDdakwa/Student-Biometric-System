package zw.co.biometricwebcore.request;

import lombok.Data;

@Data
public class TextRequest {
    private String destination; // Recipient's mobile number
    private String messageText;  // The message content
    private String originator;    // Sender ID
}
