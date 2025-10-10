package zw.co.biometricwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.biometricwebcore.domain.model.SMSNotifications;

public interface SMSRepository extends JpaRepository<SMSNotifications, Long> {
    // Custom query methods can be defined here if needed
}