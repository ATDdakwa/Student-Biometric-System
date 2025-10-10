package zw.co.biometricwebcore.domain.repository;

import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zw.co.biometricwebcore.domain.model.Patient;
import zw.co.biometricwebcore.domain.model.Status;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    // You can define custom query methods here if needed

    // Example: Find a patient by their personnel number
    Optional <Patient> findByPersonnelNumber(String personnelNumber);

    List<Patient> findByPersonnelNumberIn(List<String> personnelNumbers);

    Optional<Patient> findByIdNumber(String idNumber);

    // Example: Find all patients by status
    List<Patient> findByStatus(Status status);

    @Query("SELECT p FROM Patient p WHERE SUBSTRING(p.idNumber, 1, 9) = :idNumber")
    Optional<Patient> findByIdNumberPrefix(@Param("idNumber") String idNumber);

    Page<Patient> findAllByGender(String male, Pageable pageable);

    List<Patient> findAllByCompany(String company);



    @Query("SELECT DISTINCT p.department FROM Patient p")
    List<String> findDistinctDepartments();
    @Query("SELECT DISTINCT p.division FROM Patient p")
    List<String> findDistinctDivisions();

    // You can add more custom methods as required
}