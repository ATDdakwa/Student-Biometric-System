package zw.co.biometricwebcore.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    Active,
    Inactive,
    Deceased;


    @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static Status fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return Active; // Default value if empty
        }
        return Status.valueOf(value.trim()); // Convert to enum (case-sensitive)
    }
}