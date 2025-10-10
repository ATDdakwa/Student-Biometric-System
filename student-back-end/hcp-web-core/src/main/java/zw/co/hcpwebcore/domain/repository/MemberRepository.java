package zw.co.hcpwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zw.co.hcpwebcommons.domain.value.IdNumber;
import zw.co.hcpwebcommons.domain.value.MobileNumber;
import zw.co.hcpwebcore.domain.model.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByPolicyNumber(@Param("policyNumber") String policyNumber);

    Optional<Member> findByMsisdn(@Param("msisdn") MobileNumber msisdn);

    Optional<Member> findByNationalId(@Param("msisdn") IdNumber nationalId);

    @Query("SELECT e FROM  Member e WHERE e.isActive=true")
    List<Member> findAllByActive(boolean isActive);
}