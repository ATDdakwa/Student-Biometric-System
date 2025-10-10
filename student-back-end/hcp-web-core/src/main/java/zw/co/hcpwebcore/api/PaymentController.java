package zw.co.hcpwebcore.api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.hcpwebcommons.api.ApiResponse;
import zw.co.hcpwebcore.domain.response.PaymentResponse;
import zw.co.hcpwebcore.domain.service.PaymentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;


    @PutMapping("/edit/status")
    public  ApiResponse updatePaymentStatus(@RequestParam("reference") java.lang.String reference, @RequestParam String status){
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), paymentService.updatePaymentStatus(reference, status));

    }
//    @GetMapping("/get/")
//    @ApiOperation(value = "Get all payments")
//    public ApiResponse getAllPayments(){
//        return   new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),  paymentService.findAllPayments());
//    }

    @GetMapping(path = "/get/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all payments")
    public ResponseEntity<List<PaymentResponse>> findAllPayments() {
        List<PaymentResponse> paymentResponses = paymentService.findAllPayments();
        return ResponseEntity.ok(paymentResponses);
    }

    @GetMapping("/get/reference")
    @ApiOperation(value = "Get single payment by reference number")
    public ApiResponse getPaymentByReference(@RequestParam("referenceNumber") java.lang.String reference){
        return   new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),  paymentService.findPaymentByReference(reference));
    }

    @GetMapping("/get/all/policy-number")
    @ApiOperation(value = "Get all payments by policy number")
    public ApiResponse getAllPaymentsByPolicyNumber(@RequestParam("policyNumber") java.lang.String policyNumber){
        return   new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),  paymentService.findAllPaymentsByPolicyNumber(policyNumber));
    }

    @GetMapping("/get/all/status")
    @ApiOperation(value = "Get all payments by payment status")
    public ApiResponse getPaymentsByPaymentStatus(@RequestParam String status){
        return   new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),  paymentService.findAllPaymentsByStatus(status));
    }
}
