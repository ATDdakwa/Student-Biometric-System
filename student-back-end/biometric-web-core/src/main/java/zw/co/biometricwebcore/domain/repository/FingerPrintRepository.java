package zw.co.biometricwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.biometricwebcore.domain.model.AuditTrail;
import zw.co.biometricwebcore.domain.model.PatientFingerPrint;

import java.util.List;

public interface FingerPrintRepository extends JpaRepository<PatientFingerPrint, Long> {
    PatientFingerPrint findByPersonnelNumberPlusSuffix(String memberNum);
}
