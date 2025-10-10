package zw.co.firstmutual.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zw.co.firstmutual.userservice.domain.model.AuditTrail;
import zw.co.firstmutual.userservice.domain.repository.AuditTrailRepository;
import zw.co.hcpwebcommons.domain.enums.ExceptionCode;
import zw.co.hcpwebcommons.exceptions.AuditTrailNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuditTrailService {

    private final AuditTrailRepository auditTrailRepository;

    public AuditTrail add(AuditTrail auditTrail) {
        return auditTrailRepository.save(auditTrail);
    }

    public AuditTrail getById(Long id) {
        Optional<AuditTrail> fromDatabaseAuditTrail = auditTrailRepository.findById(id);

        if (!fromDatabaseAuditTrail.isPresent())
            throw new AuditTrailNotFoundException("AuditTrail with ID " + id + " not found!", ExceptionCode.AUDIT_TRAIL_NOT_FOUND);
        return fromDatabaseAuditTrail.get();
    }

    public List<AuditTrail> getAll() {
        return auditTrailRepository.findAll();
    }

    public List<AuditTrail> getByUserId(Long userId) {
        return auditTrailRepository.findByUser(userId);
    }
}