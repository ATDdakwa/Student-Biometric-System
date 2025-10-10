package zw.co.biometricwebcore.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SuspensionReason {
    Coe_Expired,
    Under_Investigation,
    Advance_payment,
    Banking_Details,
    Missing_Details;

    @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static SuspensionReason fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return Coe_Expired; // Default value if empty
        }
        return SuspensionReason.valueOf(value.trim()); // Convert to enum (case-sensitive)
    }
}
