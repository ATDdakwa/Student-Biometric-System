package zw.co.hcpwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zw.co.hcpwebcore.domain.model.Policy;

import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy,Long> {

    Optional<Policy> findPolicyByPolicyNumber(@Param("policyNumber") String policyNumber);
}
