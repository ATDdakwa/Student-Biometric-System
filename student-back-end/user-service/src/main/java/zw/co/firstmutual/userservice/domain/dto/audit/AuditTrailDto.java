package zw.co.firstmutual.userservice.domain.dto.audit;

import zw.co.firstmutual.userservice.domain.model.User;

public record AuditTrailDto(long id, User user) {
}