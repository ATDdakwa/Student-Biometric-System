package zw.co.biometricwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.biometricwebcore.domain.model.StudentFingerPrint;

public interface FingerPrintRepository extends JpaRepository<StudentFingerPrint, Long> {
    StudentFingerPrint findByPersonnelNumberPlusSuffix(String memberNum);
}
