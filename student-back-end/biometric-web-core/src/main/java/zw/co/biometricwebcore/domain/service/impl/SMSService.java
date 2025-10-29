package zw.co.biometricwebcore.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import zw.co.biometricwebcore.domain.repository.SMSRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zw.co.biometricwebcore.request.TextRequest;

import java.util.Base64;

@Service
public class SMSService {

    private static final Logger logger = LoggerFactory.getLogger(SMSService.class);

    private final String SMS_GATEWAY_URL = "https://mobile.esolutions.co.zw/bmg/api/single/";
    private final String SMS_GATEWAY_USERNAME = "fmlapi";
    private final String SMS_GATEWAY_PASSWORD = "cs8GGcjW";
    private final String SMS_GATEWAY_ORIGINATOR = "FirstMutual";

    @Autowired
    private SMSRepository smsRepository;

    public void sendSMS(String mobileNumber, String message) {
        TextRequest textRequest = new TextRequest();
        textRequest.setDestination(mobileNumber);
        textRequest.setMessageText(message);
        textRequest.setOriginator(SMS_GATEWAY_ORIGINATOR); // From your gateway settings

        HttpHeaders headers = new HttpHeaders();
        String auth = SMS_GATEWAY_USERNAME + ":" + SMS_GATEWAY_PASSWORD;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON); // Set content type if needed

        HttpEntity<TextRequest> requestEntity = new HttpEntity<>(textRequest, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            // Send the SMS request to the SMS gateway
            ResponseEntity<String> response = restTemplate.exchange(SMS_GATEWAY_URL, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("SMS sent successfully to {}", mobileNumber);
            } else {
                logger.error("Failed to send SMS: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Failed to send SMS: {}", e.getMessage());
        }
    }
}