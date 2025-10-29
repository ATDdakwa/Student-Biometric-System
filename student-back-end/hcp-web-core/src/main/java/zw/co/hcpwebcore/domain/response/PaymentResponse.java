package zw.co.hcpwebcore.domain.response;


import java.time.Instant;

public record PaymentResponse(java.lang.String reference, java.lang.String policyNumber, java.lang.String customerNumber, java.lang.String mobileNumber, double amount,Instant createdDate, String status,String firstName, String lastName) {
}
