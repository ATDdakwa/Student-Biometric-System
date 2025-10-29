package zw.co.biometricwebcore.domain.repository;

import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zw.co.biometricwebcore.domain.model.StudentOldModel;
import zw.co.biometricwebcore.domain.model.Status;

import java.util.List;
import java.util.Optional;

public interface StudentOldRepository extends JpaRepository<StudentOldModel, Long> {
    // You can define custom query methods here if needed

    // Example: Find a patient by their personnel number
    Optional <StudentOldModel> findByPersonnelNumber(String personnelNumber);
    Optional<StudentOldModel> findByIdNumber(String idNumber);

    // Example: Find all patients by status
    List<StudentOldModel> findByStatus(Status status);

    @Query("SELECT p FROM StudentOldModel p WHERE SUBSTRING(p.idNumber, 1, 9) = :idNumber")
    Optional<StudentOldModel> findByIdNumberPrefix(@Param("idNumber") String idNumber);

    Page<StudentOldModel> findAllByGender(String male, Pageable pageable);

    List<StudentOldModel> findAllByCompany(String company);


    List<StudentOldModel> findByPersonnelNumberIn(List<String> personnelNumbers);

    @Query("SELECT DISTINCT p.department FROM StudentOldModel p")
    List<String> findDistinctDepartments();
    @Query("SELECT DISTINCT p.division FROM StudentOldModel p")
    List<String> findDistinctDivisions();

    // You can add more custom methods as required
}