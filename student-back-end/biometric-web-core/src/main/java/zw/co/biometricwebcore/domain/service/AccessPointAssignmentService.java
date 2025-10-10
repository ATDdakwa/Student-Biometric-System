package zw.co.biometricwebcore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.biometricwebcore.domain.model.AccessPoint;
import zw.co.biometricwebcore.domain.model.AccessPointAssignment;
import zw.co.biometricwebcore.domain.repository.AccessPointAssignmentRepository;
import zw.co.biometricwebcore.domain.repository.AccessPointRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccessPointAssignmentService {

    private final AccessPointAssignmentRepository assignmentRepository;
    private final AccessPointRepository accessPointRepository;

    @Transactional
    public AccessPointAssignment assign(String username, String accessPointCode) {
        AccessPoint ap = accessPointRepository.findByCode(accessPointCode)
                .orElseThrow(() -> new IllegalArgumentException("Access point not found"));
        Optional<AccessPointAssignment> existing = assignmentRepository.findByUsername(username);
        AccessPointAssignment assignment = existing.orElseGet(() -> AccessPointAssignment.builder().username(username).build());
        assignment.setAccessPoint(ap);
        assignment.setActive(true);
        return assignmentRepository.save(assignment);
    }

    public Optional<AccessPointAssignment> getByUsername(String username) {
        return assignmentRepository.findByUsername(username);
    }

    @Transactional
    public void unassign(String username) {
        assignmentRepository.deleteByUsername(username);
    }
}
