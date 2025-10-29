package zw.co.biometricwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.biometricwebcore.domain.model.Dependant;

import java.util.List;
import java.util.Optional;

public interface StudentsDepRepository extends JpaRepository<Dependant, Long> {
    List <Dependant> findByPersonnelNumber(String personnelNumber);

    List<Dependant> findByPersonnelNumberInAndSuffixIn(List<String> personnelNumbers, List<Integer> suffixes);
    Optional<Dependant> findByPersonnelNumberAndSuffix(String personnelNumber, int suffix);
}