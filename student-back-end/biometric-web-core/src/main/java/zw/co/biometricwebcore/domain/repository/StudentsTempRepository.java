package zw.co.biometricwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zw.co.biometricwebcore.domain.model.StudentsTemp;

import javax.transaction.Transactional;

@Repository
public interface StudentsTempRepository extends JpaRepository<StudentsTemp, Long> {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE patients_temp", nativeQuery = true)
    void truncateTable();


}
