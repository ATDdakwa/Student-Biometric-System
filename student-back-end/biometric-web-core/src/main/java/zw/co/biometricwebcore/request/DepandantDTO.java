package zw.co.biometricwebcore.request;



import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;

@Data
public class DepandantDTO {
    private Long id;
    private String personnelNumber;
    private String fullName;
    private String dob;
    private String gender;
    private Integer suffix;
    private String relation;
    private String idNumber;
    private Integer age;



}