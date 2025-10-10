package zw.co.biometricwebcore.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Company {
    ZIMBABWE_SUGAR_ASSOCIATION("7210 - Zimbabwe Sugar Association"),
    HIPPO_VALLEY_ESTATES("7300 - Hippo Valley Estates Ltd"),
    TRIANGLE_LIMITED("7400 - Triangle Limited");

    private final String value;

    Company(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Company fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return TRIANGLE_LIMITED; // Default value if empty
        }
        for (Company company : Company.values()) {
            if (company.getValue().equals(value.trim())) {
                return company; // Return matching enum
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value); // Handle unknown value
    }
}