package zw.co.biometricwebcore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.biometricwebcore.domain.model.AccessPoint;
import zw.co.biometricwebcore.domain.model.Student;
import zw.co.biometricwebcore.domain.model.StudentAccessEvent;
import zw.co.biometricwebcore.domain.repository.AccessPointRepository;
import zw.co.biometricwebcore.domain.repository.StudentAccessEventRepository;
import zw.co.biometricwebcore.domain.repository.StudentRepository;
import zw.co.biometricwebcore.domain.repository.AccessPointAssignmentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final StudentRepository studentRepository;
    private final AccessPointRepository accessPointRepository;
    private final StudentAccessEventRepository eventRepository;
    private final AccessPointAssignmentRepository assignmentRepository;

    @Transactional
    public StudentAccessEvent verifyAndRecord(String studentNumber, String accessPointCode, String direction, String biometricTag, String verifierUsername) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        AccessPoint accessPoint;
        if (accessPointCode != null && !accessPointCode.isBlank()) {
            accessPoint = accessPointRepository.findByCode(accessPointCode)
                    .orElseThrow(() -> new IllegalArgumentException("Access point not found"));
        } else if (verifierUsername != null && !verifierUsername.isBlank()) {
            var assignment = assignmentRepository.findByUsername(verifierUsername)
                    .orElseThrow(() -> new IllegalArgumentException("No access point assignment for username"));
            if (assignment.getActive() == null || !assignment.getActive()) {
                throw new IllegalArgumentException("Assignment is inactive for username");
            }
            accessPoint = assignment.getAccessPoint();
            if (accessPoint == null) {
                throw new IllegalArgumentException("Assigned access point not found for username");
            }
        } else {
            throw new IllegalArgumentException("Access point code or verifierUsername is required");
        }

        boolean registered = "REGISTERED".equalsIgnoreCase(student.getEnrolmentStatus());
        boolean biometricOk = student.getIsBiometric() != null && student.getIsBiometric();

        boolean allowed = registered && biometricOk && Boolean.TRUE.equals(accessPoint.getActive());

        StudentAccessEvent event = StudentAccessEvent.builder()
                .student(student)
                .accessPoint(accessPoint)
                .timestamp(LocalDateTime.now())
                .direction(direction)
                .verified(allowed)
                .reason(allowed ? null : (registered ? "Biometric mismatch or inactive access point" : "Not registered"))
                .build();
        return eventRepository.save(event);
    }

    public List<StudentAccessEvent> getStudentHistory(Long studentId) {
        return eventRepository.findByStudentId(studentId);
    }

    public List<StudentAccessEvent> getStudentHistoryBetween(Long studentId, LocalDateTime from, LocalDateTime to) {
        return eventRepository.findByStudentIdAndTimestampBetween(studentId, from, to);
    }

    public List<StudentAccessEvent> getEventsBetween(LocalDateTime from, LocalDateTime to) {
        return eventRepository.findAllBetween(from, to);
    }
}
