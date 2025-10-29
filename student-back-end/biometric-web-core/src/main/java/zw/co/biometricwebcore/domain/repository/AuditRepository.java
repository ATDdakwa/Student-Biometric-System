package zw.co.biometricwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.biometricwebcore.domain.model.AuditTrail;

public interface AuditRepository extends JpaRepository<AuditTrail, Long> {
}
