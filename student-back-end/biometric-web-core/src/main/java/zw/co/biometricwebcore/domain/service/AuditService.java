package zw.co.biometricwebcore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.model.AuditTrail;
import zw.co.biometricwebcore.domain.repository.AuditRepository;
import zw.co.biometricwebcore.domain.repository.PatientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository auditRepository;
    public AuditTrail createTrail(AuditTrail auditTrail) {
        return auditRepository.save(auditTrail);
    }

    public List<AuditTrail> getAllLogs() {
        return  auditRepository.findAll();
    }
}
