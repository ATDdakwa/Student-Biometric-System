package zw.co.biometricwebcore.api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zw.co.biometricwebcore.domain.model.Dependant;
import zw.co.biometricwebcore.domain.model.Patient;
import zw.co.biometricwebcore.domain.service.PatientService;
import zw.co.biometricwebcore.domain.service.SMSService;
import zw.co.biometricwebcore.request.DepandantDTO;
import zw.co.biometricwebcore.request.PatientDTO;
import zw.co.biometricwebcore.request.TextRequest;
import zw.co.biometricwebcore.response.BaseResult;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;
    private final SMSService smsService;

    @GetMapping(path = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all client details with pagination")
    public PatientService.PatientPageResponse getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "17000") int size) {
        return patientService.getPatientsPaginated(page, size);
    }


    @GetMapping(path = "/get-all-reports", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all client details with pagination")
    public List<Patient> findAllPatients() {
        return patientService.findAllPatients();
    }

    @GetMapping(path = "/get-all-male", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all patients details with pagination")
    public PatientService.PatientPageResponse getAllMalePatient(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "17000") int size) {
        return patientService.getPatientsMalePaginated(page, size);
    }

    @GetMapping("/api/v1/patients/get-reports")
    public ResponseEntity<?> getAllPatients(
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String division,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String medicalAid
    ) {
        try {

            List<Patient> patients = patientService.fetchPatients(company, division, department, medicalAid);

            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching patients: " + e.getMessage());
        }
    }

    @GetMapping(path = "/get-all-female", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all patients details with pagination")
    public PatientService.PatientPageResponse getPatientsFemalePaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "17000") int size) {
        return patientService.getPatientsFemalePaginated(page, size);
    }

    @GetMapping("/distinct-departments")
    public List<String> getDistinctDepartments() {
        return patientService.getDistinctDepartments();
    }

    @GetMapping("/distinct-divisions")
    public List<String> getDistinctDivisions() {
        return patientService.getDistinctDivisions();
    }

    private PatientDTO convertToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();

        dto.setPersonnelNumber(patient.getPersonnelNumber());
        dto.setFirstName(patient.getFirstName());
        dto.setSurname(patient.getSurname()); // Updated to match DTO field
        dto.setDob(patient.getDob()); // Include Date of Birth
        dto.setMaritalStatus(patient.getMaritalStatus()); // Convert enum to String
        dto.setIdNumber(patient.getIdNumber());
        dto.setGender(patient.getGender()); // Convert enum to String
        dto.setStatus(patient.getStatus() ); // Convert enum to String
        dto.setAge(patient.getAge()); // Include age directly
        dto.setCompany(patient.getCompany() ); // Convert enum to String
        dto.setScheme(patient.getScheme() ); // Convert enum to String
//        dto.setRelation(patient.getRelation());
//        dto.setSuffix(Integer.valueOf(patient.getSuffix()));
//        dto.setPatientType(patient.getPatientType());


        dto.setIsBiometric(patient.getIsBiometric());
        dto.setBiometricTag(patient.getBiometric_tag()); // Renamed for consistency
        dto.setCoeDocumentPath(patient.getCoeDocumentPath()); // Include COE document path

        return dto;
    }
    @GetMapping("/{id}")
    public Patient getPensionerById(@PathVariable Long id) {
        return patientService.getPatientById((id));
    }

    @PostMapping("/create")
    public Patient createPatient(@RequestBody Patient patient) {
        return patientService.createPatient(patient);
    }

    
    //updating biometric tag
    @PutMapping("/{idNumber}")
    public ResponseEntity<?> updatePensionerBiometric(
            @PathVariable String idNumber, // Change to String
            @RequestBody Patient patient) {
        try {
            // Update the pensioner using the national ID
            Patient updatedPatient = patientService.updatePatientBiometric(idNumber, patient);
            return ResponseEntity.ok(updatedPatient);
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
            String biometricTag = patientService.getBiometricTagByIdNumber(idNumber);
            return ResponseEntity.ok(biometricTag);
        } catch (PatientController.PatientNotFoundException e) {
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

            Patient patient = patientService.getPatientByIdNumber(trimmedIdNumber);
            PatientDTO patientDTO = convertToDTO(patient);

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


            Patient patient = patientService.getByPersonnelNumber(personnelNumber);
            PatientDTO patientDTO = convertToDTO(patient);

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
            Patient patient = patientService.getPatientByIdNumber(idNumber);
            patientService.suspendPatientIfRequired(patient);
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
        patientService.updateCoeDate(nationalId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dependants/{personnelNumber}")
    public ResponseEntity<BaseResult> getDependants(@PathVariable String personnelNumber) {
        return patientService.getDependants(personnelNumber);
    }

    @GetMapping("/dependants")
    public ResponseEntity<BaseResult> getAllDependants() {
        return patientService.getFindAllDependants();
    }


    @GetMapping("/dependants/{personnelNumber}/{suffix}")
    public ResponseEntity<BaseResult> getDependants(@PathVariable String personnelNumber,@PathVariable int suffix) {
        return patientService.getDependantByPersonnelAndSuffix(personnelNumber,suffix);
    }

}