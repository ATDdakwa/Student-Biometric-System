package zw.co.hcpwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zw.co.hcpwebcore.domain.model.Payment;

import java.util.List;
import java.util.Optional;


public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findPaymentByReference(@Param("reference") java.lang.String reference);

    List<Payment> findAllByPolicyNumber(@Param("policyNumber") java.lang.String policyNumber);

    List<Payment> findAllByStatus(@Param("status") String status);

    List<Payment> findAllByOrderByCreatedDateDesc();


    @Query(value = "SELECT * FROM Payment p ORDER BY p.created_date DESC", nativeQuery = true)
    List<Payment> findAllPayments();
}
