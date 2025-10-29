package zw.co.biometricwebcore.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TerminationReason {
    None,
    Death,
    Full_Commutation,
    Child_Maturity;

    @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static TerminationReason fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return None; // Default value if empty
        }
        return TerminationReason.valueOf(value.trim()); // Convert to enum (case-sensitive)
    }
}