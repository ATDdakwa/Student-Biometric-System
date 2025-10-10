package zw.co.biometricwebcore.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zw.co.biometricwebcore.domain.model.StudentAccessEvent;
import zw.co.biometricwebcore.domain.model.Student;

import java.time.LocalDateTime;
import java.util.List;

public interface StudentAccessEventRepository extends JpaRepository<StudentAccessEvent, Long> {
    List<StudentAccessEvent> findByStudentId(Long studentId);
    List<StudentAccessEvent> findByStudentIdAndTimestampBetween(Long studentId, LocalDateTime from, LocalDateTime to);

    @Query("SELECT e FROM StudentAccessEvent e WHERE e.timestamp >= :from AND e.timestamp <= :to")
    List<StudentAccessEvent> findAllBetween(LocalDateTime from, LocalDateTime to);
}
