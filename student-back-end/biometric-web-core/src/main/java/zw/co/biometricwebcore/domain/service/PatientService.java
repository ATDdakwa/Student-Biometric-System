package zw.co.biometricwebcore.domain.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zw.co.biometricwebcore.api.PatientController;
import zw.co.biometricwebcore.domain.model.Dependant;
import zw.co.biometricwebcore.domain.model.Patient;
import zw.co.biometricwebcore.domain.model.Status;
import zw.co.biometricwebcore.domain.model.SuspensionReason;
import zw.co.biometricwebcore.domain.repository.DependantRepository;
import zw.co.biometricwebcore.domain.repository.PatientRepository;
import zw.co.biometricwebcore.request.PatientDTO;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zw.co.biometricwebcore.response.BaseResult;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper; // assuming you're using ModelMapper
    private final DependantRepository dependantRepository;
    @Autowired
    private SMSService smsService;


    public List<PatientDTO> getAllPatients() {
        try {
            List<Patient> patientList = patientRepository.findAll();
            return patientList.stream()
                    .map(patients -> modelMapper.map(patients, PatientDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // log the error or throw a custom exception
            return Collections.emptyList();
        }
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById((id)).orElseThrow();
    }

    /**
     * Create a new pensioner
     *
     * @param patient Pensioner entity to create
     * @return Created pensioner entity
     */
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }



    public Patient updatePatientBiometric(String idNumber, Patient patient) {
        // Find the existing patient by ID number
        Patient existingPatient = patientRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new PatientController.PatientNotFoundException("Pensioner not found"));

        // Update biometric fields
        existingPatient.setBiometric_tag(patient.getBiometric_tag());
        existingPatient.setIsBiometric(patient.getIsBiometric());


        // Set COE raise date to the current date
        LocalDate currentDate = LocalDate.now();

        // Set COE expected date to one year later
        LocalDate expectedDate = currentDate.plusYears(1);

        // Update next payment date based on COE date
        updateNextPaymentDate(existingPatient);

        // Save the updated patient
        return patientRepository.save(existingPatient);
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    /**
     * Delete a pensioner
     *
     * @param id ID of the pensioner to delete
     */
    public void deletePatient(Long id) {
        patientRepository.deleteById((id));
    }

    public Patient findPensionerById(Long nationalId) {
        return patientRepository.findById(nationalId).orElse(null); // Return null if not found
    }

    public String getBiometricTagByIdNumber(String idNumber) {
        // Use Optional to find the patient
        Optional<Patient> optionalPatient = patientRepository.findByIdNumber(idNumber);

        // Check if the patient is present and handle accordingly
        Patient patient = optionalPatient.orElseThrow(() -> new PatientController.PatientNotFoundException("Pensioner not found"));

        // Return the biometric tag from the patient
        return patient.getBiometric_tag();
    }

    public Patient getPatientByIdNumber(String idNumber) {
        // Trim and get the first 9 characters
        String trimmedIdNumber = idNumber.trim().substring(0, 9);
        System.out.println("Fetching patient with ID number prefix: " + trimmedIdNumber); // Log the ID number
        return patientRepository.findByIdNumberPrefix(trimmedIdNumber)
                .orElseThrow(() -> new PatientController.PatientNotFoundException("Patient not found"));
    }





    private void updateNextPaymentDate(Patient patient) {
//        // Convert coeDate to LocalDate
//        LocalDate coeRaiseDate = patient.getCoeRaiseDate().toInstant()
//                .atZone(ZoneId.systemDefault())
//                .toLocalDate();
//
//        if (coeRaiseDate.getDayOfMonth() < 10) {
//            // If COE date is less than 10, set next payment date to the last day of this month
//            LocalDate lastDayOfMonth = coeRaiseDate.withDayOfMonth(coeRaiseDate.lengthOfMonth());
//            patient.setNextPaymentDate(Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        } else {
//            // If COE date is 10 or greater, set next payment date to the last day of the next month
//            LocalDate lastDayOfNextMonth = coeRaiseDate.plusMonths(1).withDayOfMonth(1).plusMonths(1).minusDays(1);
//            patient.setNextPaymentDate(Date.from(lastDayOfNextMonth.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        }
    }





    public boolean suspendPatientIfRequired(Patient patient) {
//        Date coeDate = patient.getCoeDate();
//        if (coeDate != null) {
//            LocalDate coeLocalDate = coeDate.toInstant()
//                    .atZone(ZoneId.systemDefault())
//                    .toLocalDate();
//
//            // Check if the current date exceeds the coeDate
//            if (LocalDate.now().isAfter(coeLocalDate)) {
//                 // Assuming Status is an Enum
//                patient.setIsCoe(false); // Set isCoe to false
//                patient.setIsBiometric(false); // Set isBiometric to false
//                patient.setSuspensionReason(SuspensionReason.Coe_Expired);
//
//                patientRepository.save(patient); // Save updated pensioner
//
//                return true; // Indicate that the pensioner was suspended
//            }
//        }
        return false; // Indicate that the pensioner was not suspended
    }








    public void updateCoeDate(String idNumber) {
        Patient patient = patientRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new PatientController.PatientNotFoundException("Pensioner not found"));

        // Set COE date to current date
        LocalDate currentDate = LocalDate.now();

        // Set COE expected date to one year later
        LocalDate expectedDate = currentDate.plusYears(1);

        updateNextPaymentDate(patient);

        // Save the updated pensioner
        patientRepository.save(patient);
    }

    public PatientPageResponse getPatientsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patientPage = patientRepository.findAll(pageable);

        return new PatientPageResponse(patientPage.getContent().stream()
                .map(patients -> modelMapper.map(patients, PatientDTO.class))
                .collect(Collectors.toList()),
                patientPage.getTotalElements());
    }

    public PatientPageResponse getPatientsMalePaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Filter by gender where gender is Male
        Page<Patient> patientPage = patientRepository.findAllByGender("Male", pageable);

        return new PatientPageResponse(patientPage.getContent().stream()
                .map(patient -> modelMapper.map(patient, PatientDTO.class))
                .collect(Collectors.toList()),
                patientPage.getTotalElements());
    }

    public PatientPageResponse getPatientsFemalePaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Filter by gender where gender is Male
        Page<Patient> patientPage = patientRepository.findAllByGender("Female", pageable);

        return new PatientPageResponse(patientPage.getContent().stream()
                .map(patient -> modelMapper.map(patient, PatientDTO.class))
                .collect(Collectors.toList()),
                patientPage.getTotalElements());
    }

    public List<Patient> fetchPatients(String company, String division, String department, String medicalAid) {
        return patientRepository.findAll();
    }

    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }

    public List<String> getDistinctDepartments() {
        return patientRepository.findDistinctDepartments();
    }

    public List<String> getDistinctDivisions() {
        return patientRepository.findDistinctDivisions();
    }

    public Patient getByPersonnelNumber(String personnelNumber) {
        return patientRepository.findByPersonnelNumber(personnelNumber)
                .orElseThrow(() -> new PatientController.PatientNotFoundException("Patient not found"));
    }




    public class PatientPageResponse {

        private List<PatientDTO> patients;
        private long totalElements;

        // Constructor
        public PatientPageResponse(List<PatientDTO> patients, long totalElements) {
            this.patients = patients;
            this.totalElements = totalElements;
        }

        // Getters
        public List<PatientDTO> getPatients() {
            return patients;
        }

        public long getTotalElements() {
            return totalElements;
        }

        // Optionally, you can also add setters if needed
        public void setPatients(List<PatientDTO> patients) {
            this.patients = patients;
        }

        public void setTotalElements(long totalElements) {
            this.totalElements = totalElements;
        }
    }

    public ResponseEntity<BaseResult> getDependants(String personnelNumber) {
        List<Dependant> dependants = dependantRepository.findByPersonnelNumber(personnelNumber);
        return ResponseEntity.ok(new BaseResult(dependants, "Dependants fetched successfully",
                "00"));
    }


    public ResponseEntity<BaseResult> getDependantByPersonnelAndSuffix(String personnelNumber, int suffix) {
        Dependant dependants = dependantRepository.findByPersonnelNumberAndSuffix(personnelNumber,suffix);
        return ResponseEntity.ok(new BaseResult(dependants, "Dependant fetched successfully",
                "00"));
    }

    public ResponseEntity<BaseResult> getFindAllDependants() {
        List<Dependant> dependants = dependantRepository.findAll();
        return ResponseEntity.ok(new BaseResult(dependants, "Dependant fetched successfully",
                "00"));
    }
}