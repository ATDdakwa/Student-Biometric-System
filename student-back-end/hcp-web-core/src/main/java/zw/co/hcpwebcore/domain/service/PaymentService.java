package zw.co.hcpwebcore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zw.co.hcpwebcommons.domain.enums.ExceptionCode;
import zw.co.hcpwebcommons.domain.value.MobileNumber;
import zw.co.hcpwebcommons.exceptions.PaymentNotFoundException;
import zw.co.hcpwebcommons.exceptions.PaymentsNotFoundException;
import zw.co.hcpwebcore.domain.repository.PaymentRepository;
import zw.co.hcpwebcore.domain.response.PaymentResponse;
import zw.co.hcpwebcore.domain.model.Member;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final MemberService memberService;

   public List<PaymentResponse> findAllPayments() {

       var payments = paymentRepository.findAllPayments();

//       var customer = memberService.findMemberByMsisdn(new MobileNumber(payment.getCustomerNumber()));

//       var payments = paymentRepository.findAllByOrderByCreatedDateDesc();

       if (payments.isEmpty())
           throw new PaymentsNotFoundException("Payments Not Found.", ExceptionCode.PAYMENTS_UNAVAILABLE);

//        return payments.stream().map(payment -> new PaymentResponse(payment.getReference(), payment.getPolicyNumber(), payment.getCustomerNumber(), payment.getMobileNumber(), payment.getAmount()
//                ,payment.getCreatedDate(),payment.getStatus())).collect(Collectors.toList());
//    }
       return payments.stream()
               .filter(payment -> payment.getMobileNumber() != null && !payment.getMobileNumber().isEmpty())
               .flatMap(payment -> {
                   Member member = memberService.findMemberByMsisdn(new MobileNumber(payment.getMobileNumber()));
                   return Stream.of(new PaymentResponse(
                           payment.getReference(),
                           payment.getPolicyNumber(),
                           payment.getCustomerNumber(),
                           payment.getMobileNumber(),
                           payment.getAmount(),
                           payment.getCreatedDate(),
                           payment.getStatus(),
                           member.getFirstName(),
                           member.getLastName()
                   ));
               })
               .collect(Collectors.toList());
   }
    public PaymentResponse findPaymentByReference(java.lang.String reference){
       var payment = paymentRepository.findPaymentByReference(reference);

       if (payment.isEmpty()) throw new PaymentNotFoundException("Payment with reference: "+ reference +" not found.", ExceptionCode.PAYMENT_NOT_FOUND);

       return new PaymentResponse(payment.get().getReference(), payment.get().getPolicyNumber(), payment.get().getCustomerNumber(), payment.get().getMobileNumber(), payment.get().getAmount()
               ,payment.get().getCreatedDate(),payment.get().getStatus(),"","");
    }

    public List<PaymentResponse> findAllPaymentsByPolicyNumber(java.lang.String policyNumber) {

        var payments = paymentRepository.findAllByPolicyNumber(policyNumber);

        if (payments.isEmpty())
            throw new PaymentsNotFoundException("Payments under policy number: "+ policyNumber + " Not Found.", ExceptionCode.PAYMENTS_UNAVAILABLE);

        return payments.stream().map(payment -> new PaymentResponse(payment.getReference(), payment.getPolicyNumber(), payment.getCustomerNumber(), payment.getMobileNumber(), payment.getAmount()
                ,payment.getCreatedDate(),payment.getStatus(),"","")).collect(Collectors.toList());
    }

    public List<PaymentResponse> findAllPaymentsByStatus(String status) {

        var payments = paymentRepository.findAllByStatus(status);

        if (payments.isEmpty())
            throw new PaymentsNotFoundException("Payments with status: "+ status+ " Not Found.", ExceptionCode.PAYMENTS_UNAVAILABLE);

        return payments.stream().map(payment -> new PaymentResponse(payment.getReference(), payment.getPolicyNumber(), payment.getCustomerNumber(), payment.getMobileNumber(), payment.getAmount()
                ,payment.getCreatedDate(),payment.getStatus(),"","")).collect(Collectors.toList());
    }

    public java.lang.String updatePaymentStatus(java.lang.String reference, String status){

       var payment = paymentRepository.findPaymentByReference(reference);

       if (payment.isEmpty()) throw new PaymentNotFoundException("Payment with reference: "+ reference+" not found.", ExceptionCode.PAYMENT_NOT_FOUND);

       payment.get().setStatus(status);

       paymentRepository.save(payment.get());

       return "Payment status successfully updated";
    }
}
