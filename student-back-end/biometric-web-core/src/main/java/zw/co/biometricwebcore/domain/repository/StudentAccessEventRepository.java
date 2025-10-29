package zw.co.biometricwebcore.domain.repository;

import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zw.co.biometricwebcore.domain.model.StudentAccessEvent;
import zw.co.biometricwebcore.domain.model.Student;

import java.time.LocalDateTime;
import java.util.List;

public interface StudentAccessEventRepository extends JpaRepository<StudentAccessEvent, Long> {
    List<StudentAccessEvent> findByStudentId(Long studentId);
    List<StudentAccessEvent> findByStudentIdAndTimestampBetween(Long studentId, LocalDateTime from, LocalDateTime to);

    @Query("SELECT e FROM StudentAccessEvent e JOIN e.student s WHERE e.timestamp >= :from AND e.timestamp <= :to")
    List<StudentAccessEvent> findAllByTime(LocalDateTime from, LocalDateTime to);

    @Query("""
    SELECT e 
    FROM StudentAccessEvent e 
    JOIN e.student s 
    WHERE e.timestamp BETWEEN :from AND :to
    AND s.studentNumber = :reg
""")
    List<StudentAccessEvent> findAllBetween(
            @Param("reg") String reg,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to

    );

    List<StudentAccessEvent> findAllByTimestampBetween(LocalDateTime from, LocalDateTime to);

    @Query("""
    SELECT e 
    FROM StudentAccessEvent e 
    JOIN e.student s 
    WHERE e.timestamp BETWEEN :from AND :to
    AND s.studentNumber = :reg
""")
    List<StudentAccessEvent> findAllBetweenWithReg(
            @Param("reg") String reg,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

}
