package zw.co.biometricwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.biometricwebcore.domain.model.AccessPoint;

import java.util.Optional;

public interface AccessPointRepository extends JpaRepository<AccessPoint, Long> {
    Optional<AccessPoint> findByCode(String code);
    Optional<AccessPoint> findByName(String name);
}
