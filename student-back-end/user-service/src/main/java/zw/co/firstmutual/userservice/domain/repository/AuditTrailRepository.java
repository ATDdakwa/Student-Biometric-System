package zw.co.firstmutual.userservice.domain.repository;

import org.springframework.stereotype.Repository;
import zw.co.firstmutual.userservice.domain.model.AuditTrail;
import zw.co.hcpwebcommons.domain.repository.CustomRepository;

import java.util.List;

@Repository
public interface AuditTrailRepository extends CustomRepository<AuditTrail, Long> {
    List<AuditTrail> findByUser(Long userId);
}