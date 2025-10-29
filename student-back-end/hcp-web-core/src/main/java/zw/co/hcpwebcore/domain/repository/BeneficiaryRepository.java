package zw.co.hcpwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zw.co.hcpwebcommons.domain.value.IdNumber;
import zw.co.hcpwebcore.domain.model.Beneficiary;


import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary,Long> {

    List<Beneficiary> findAllByPolicyNumber(@Param("memberNumber") String policyNumber);

    Optional<Beneficiary> findByNationalId(@Param("nationalId") IdNumber idNumber);

    @Query("SELECT e FROM  Beneficiary e WHERE e.isActive=true")
    List<Beneficiary> findAllByActive(@Param("isActive") boolean isActive);

}