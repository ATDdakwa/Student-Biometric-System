package zw.co.biometricwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zw.co.biometricwebcore.domain.model.PatientsTemp;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface PatientsTempRepository extends JpaRepository<PatientsTemp, Long> {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE patients_temp", nativeQuery = true)
    void truncateTable();


}
