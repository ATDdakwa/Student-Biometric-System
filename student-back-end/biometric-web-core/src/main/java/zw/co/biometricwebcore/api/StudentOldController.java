package zw.co.biometricwebcore.api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.biometricwebcore.domain.model.StudentOldModel;
import zw.co.biometricwebcore.domain.service.impl.StudentOldService;
import zw.co.biometricwebcore.domain.service.impl.SMSService;
import zw.co.biometricwebcore.request.PatientDTO;
import zw.co.biometricwebcore.request.TextRequest;
import zw.co.biometricwebcore.response.BaseResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patients")
public class StudentOldController {

    private final StudentOldService studentOldService;
    private final SMSService smsService;

    @GetMapping(path = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all client details with pagination")
    public StudentOldService.PatientPageResponse getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "17000") int size) {
        return studentOldService.getPatientsPaginated(page, size);
    }


    @GetMapping(path = "/get-all-reports", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all client details with pagination")
    public List<StudentOldModel> findAllPatients() {
        return studentOldService.findAllPatients();
    }

    @GetMapping(path = "/get-all-male", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all patients details with pagination")
    public StudentOldService.PatientPageResponse getAllMalePatient(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "17000") int size) {
        return studentOldService.getPatientsMalePaginated(page, size);
    }

    @GetMapping("/get-reports-org")
    public ResponseEntity<?> getAllPatients(
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String division,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String medicalAid
    ) {
        try {

            List<StudentOldModel> studentOldModels = studentOldService.fetchPatients(company, division, department, medicalAid);

            return ResponseEntity.ok(studentOldModels);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching patients: " + e.getMessage());
        }
    }

    @GetMapping(path = "/get-all-female", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all patients details with pagination")
    public StudentOldService.PatientPageResponse getPatientsFemalePaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "17000") int size) {
        return studentOldService.getPatientsFemalePaginated(page, size);
    }

    @GetMapping("/distinct-departments")
    public List<String> getDistinctDepartments() {
        return studentOldService.getDistinctDepartments();
    }

    @GetMapping("/distinct-divisions")
    public List<String> getDistinctDivisions() {
        return studentOldService.getDistinctDivisions();
    }

    private PatientDTO convertToDTO(StudentOldModel studentOldModel) {
        PatientDTO dto = new PatientDTO();

        dto.setPersonnelNumber(studentOldModel.getPersonnelNumber());
        dto.setFirstName(studentOldModel.getFirstName());
        dto.setSurname(studentOldModel.getSurname()); // Updated to match DTO field
        dto.setDob(studentOldModel.getDob()); // Include Date of Birth
        dto.setMaritalStatus(studentOldModel.getMaritalStatus()); // Convert enum to String
        dto.setIdNumber(studentOldModel.getIdNumber());
        dto.setGender(studentOldModel.getGender()); // Convert enum to String
        dto.setStatus(studentOldModel.getStatus() ); // Convert enum to String
        dto.setAge(studentOldModel.getAge()); // Include age directly
        dto.setCompany(studentOldModel.getCompany() ); // Convert enum to String
        dto.setScheme(studentOldModel.getScheme() ); // Convert enum to String
        dto.setRelation(studentOldModel.getRelation());
        dto.setSuffix(Integer.valueOf(studentOldModel.getSuffix()));
        dto.setPatientType(studentOldModel.getPatientType());
        dto.setEnrolmentStatus(studentOldModel.getEnrolmentStatus());

        dto.setIsBiometric(studentOldModel.getIsBiometric());
        dto.setBiometricTag(studentOldModel.getBiometric_tag()); // Renamed for consistency
        dto.setCoeDocumentPath(studentOldModel.getCoeDocumentPath()); // Include COE document path

        return dto;
    }
    @GetMapping("/{id}")
    public StudentOldModel getPensionerById(@PathVariable Long id) {
        return studentOldService.getPatientById((id));
    }

    @PostMapping("/create")
    public StudentOldModel createPatient(@RequestBody StudentOldModel studentOldModel) {
        return studentOldService.createPatient(studentOldModel);
    }

    
    //updating biometric tag
    @PutMapping("/{idNumber}")
    public ResponseEntity<?> updatePensionerBiometric(
            @PathVariable String idNumber, // Change to String
            @RequestBody StudentOldModel studentOldModel) {
        try {
            // Update the pensioner using the national ID
            StudentOldModel updatedStudentOldModel = studentOldService.updatePatientBiometric(idNumber, studentOldModel);
            return ResponseEntity.ok(updatedStudentOldModel);
        } catch (PatientNotFoundException e) {
            return ResponseEntity.status(404).body("Pensioner not found");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }




    public static class PatientNotFoundException extends RuntimeException {
        public PatientNotFoundException(String message) {
            super(message);
        }
    }

    @GetMapping("/biometrictag/{idNumber}")
    public ResponseEntity<String> getBiometricTagByPersonnelNumber(@PathVariable String idNumber) {
        try {
            String biometricTag = studentOldService.getBiometricTagByIdNumber(idNumber);
            return ResponseEntity.ok(biometricTag);
        } catch (StudentOldController.PatientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pensioner not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/idNumber/{idNumber}")
    public ResponseEntity<PatientDTO> getPatientByIdNumber(@PathVariable String idNumber) {
        try {
            String trimmedIdNumber = idNumber.trim();
            // Log the received ID number
            System.out.println("Received idNumber: " + trimmedIdNumber);

            StudentOldModel studentOldModel = studentOldService.getPatientByIdNumber(trimmedIdNumber);
            PatientDTO patientDTO = convertToDTO(studentOldModel);

            return ResponseEntity.ok(patientDTO);
        } catch (PatientNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/personnelNumber/{personnelNumber}")
    public ResponseEntity<PatientDTO> getByPersonnelNumber(@PathVariable String personnelNumber) {
        try {


            StudentOldModel studentOldModel = studentOldService.getByPersonnelNumber(personnelNumber);
            PatientDTO patientDTO = convertToDTO(studentOldModel);

            return ResponseEntity.ok(patientDTO);
        } catch (PatientNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.status(500).body(null);
        }
    }


    @PutMapping("/suspend/{idNumber}")
    public ResponseEntity<String> suspendPensioner(@PathVariable String idNumber) {
        try {
            StudentOldModel studentOldModel = studentOldService.getPatientByIdNumber(idNumber);
            studentOldService.suspendPatientIfRequired(studentOldModel);
            return ResponseEntity.ok("Pensioner suspended and notified.");
        } catch (PatientNotFoundException e) {
            return ResponseEntity.status(404).body("Pensioner not found");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/sms/send")
    public ResponseEntity<String> sendSms(@RequestBody TextRequest textRequest) {
        // Logic to send SMS using your SMS service
        smsService.sendSMS(textRequest.getDestination(), textRequest.getMessageText());
        return ResponseEntity.ok("SMS sent successfully");
    }

    @PutMapping("/updateCoeDate/{idNumber}")
    public ResponseEntity<Void> updateCoeDate(@PathVariable String nationalId) {
        studentOldService.updateCoeDate(nationalId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dependants/{personnelNumber}")
    public ResponseEntity<BaseResult> getDependants(@PathVariable String personnelNumber) {
        return studentOldService.getDependants(personnelNumber);
    }

    @GetMapping("/dependants")
    public ResponseEntity<BaseResult> getAllDependants() {
        return studentOldService.getFindAllDependants();
    }


    @GetMapping("/dependants/{personnelNumber}/{suffix}")
    public ResponseEntity<BaseResult> getDependants(@PathVariable String personnelNumber,@PathVariable int suffix) {
        return studentOldService.getDependantByPersonnelAndSuffix(personnelNumber,suffix);
    }

}