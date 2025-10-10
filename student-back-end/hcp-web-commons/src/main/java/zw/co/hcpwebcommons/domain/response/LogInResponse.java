package zw.co.hcpwebcommons.domain.response;

import zw.co.hcpwebcommons.domain.value.Email;

public record LogInResponse(String firstName, String lastName, Email email) {
}