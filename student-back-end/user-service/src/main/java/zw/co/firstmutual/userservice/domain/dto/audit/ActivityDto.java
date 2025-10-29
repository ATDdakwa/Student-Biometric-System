package zw.co.firstmutual.userservice.domain.dto.audit;

import zw.co.firstmutual.userservice.domain.model.AuditTrail;

public record ActivityDto(Long id, Long entityId, String narrative, AuditTrail auditTrail) {

}