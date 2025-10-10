package zw.co.biometricwebcore.request;



import lombok.Data;
import zw.co.biometricwebcore.domain.model.Company;
import zw.co.biometricwebcore.domain.model.Scheme;
import zw.co.biometricwebcore.domain.model.Status;
import zw.co.hcpwebcommons.domain.enums.Gender;
import zw.co.hcpwebcommons.domain.enums.MaritalStatus;

import java.util.Date;

@Data
public class PatientDTO {
    private String personnelNumber;
    private String firstName;
    private String surname;
    private String dob;
    private String maritalStatus; // Consider using String or an enum if needed
    private String idNumber; // Renamed for consistency
    private String gender; // Consider using String or an enum if needed
    private String status; // Consider using String or an enum if needed
    private String mobileNumber;
    private Integer age; // Calculated field
    private String company;
    private String division;
    private String department;
    private String section;
    private String scheme; // Assuming Scheme is a String representation
    private String relation; // Assuming Scheme is a String representation
    private Integer suffix;
    private String patientType;
    private String enrolmentStatus;
    private Boolean isBiometric;
    private Boolean isCoe;
    private String biometricTag; // Renamed for consistency
    private String coeDocumentPath; // Path to the COE document



}