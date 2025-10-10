package zw.co.biometricwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zw.co.biometricwebcore.domain.model.DependantsTemp;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface DependantsTempRepository extends JpaRepository<DependantsTemp, Long> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE dependants_temp", nativeQuery = true)
    void truncateTable();}
