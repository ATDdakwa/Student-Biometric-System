package zw.co.biometricwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.biometricwebcore.domain.model.AccessPointAssignment;

import java.util.Optional;

public interface AccessPointAssignmentRepository extends JpaRepository<AccessPointAssignment, Long> {
    Optional<AccessPointAssignment> findByUsername(String username);
    void deleteByUsername(String username);
}
