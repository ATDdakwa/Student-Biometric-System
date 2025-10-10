package zw.co.biometricwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.biometricwebcore.domain.model.AuditTrail;
import zw.co.biometricwebcore.domain.model.Patient;

public interface AuditRepository extends JpaRepository<AuditTrail, Long> {
}
