package zw.co.biometricwebcore.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.api.StudentOldController;
import zw.co.biometricwebcore.domain.model.Dependant;
import zw.co.biometricwebcore.domain.model.StudentOldModel;
import zw.co.biometricwebcore.domain.model.Student;
import zw.co.biometricwebcore.domain.repository.StudentsDepRepository;
import zw.co.biometricwebcore.domain.repository.StudentOldRepository;
import zw.co.biometricwebcore.domain.repository.StudentRepository;
import zw.co.biometricwebcore.request.PatientDTO;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import zw.co.biometricwebcore.response.BaseResult;

@Service
@RequiredArgsConstructor
public class StudentOldService {

    private final StudentOldRepository studentOldRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper; // assuming you're using ModelMapper
    private final StudentsDepRepository studentsDepRepository;
    @Autowired
    private SMSService smsService;


    public List<PatientDTO> getAllPatients() {
        try {
            List<StudentOldModel> studentOldModelList = studentOldRepository.findAll();
            return studentOldModelList.stream()
                    .map(patients -> modelMapper.map(patients, PatientDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // log the error or throw a custom exception
            return Collections.emptyList();
        }
    }

    public StudentOldModel getPatientById(Long id) {
        return studentOldRepository.findById((id)).orElseThrow();
    }

    /**
     * Create a new pensioner
     *
     * @param studentOldModel Pensioner entity to create
     * @return Created pensioner entity
     */
    public StudentOldModel createPatient(StudentOldModel studentOldModel) {
        return studentOldRepository.save(studentOldModel);
    }



    public StudentOldModel updatePatientBiometric(String idNumber, StudentOldModel studentOldModel) {
        // Find the existing patient by ID number
        StudentOldModel existingStudentOldModel = studentOldRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new StudentOldController.PatientNotFoundException("Pensioner not found"));

        // Update biometric fields
        existingStudentOldModel.setBiometric_tag(studentOldModel.getBiometric_tag());
        existingStudentOldModel.setIsBiometric(studentOldModel.getIsBiometric());


        // Set COE raise date to the current date
        LocalDate currentDate = LocalDate.now();

        // Set COE expected date to one year later
        LocalDate expectedDate = currentDate.plusYears(1);

        // Update next payment date based on COE date
        updateNextPaymentDate(existingStudentOldModel);

        // Save the updated patient
        return studentOldRepository.save(existingStudentOldModel);
    }

    public StudentOldModel savePatient(StudentOldModel studentOldModel) {
        return studentOldRepository.save(studentOldModel);
    }

    /**
     * Delete a pensioner
     *
     * @param id ID of the pensioner to delete
     */
    public void deletePatient(Long id) {
        studentOldRepository.deleteById((id));
    }

    public StudentOldModel findPensionerById(Long nationalId) {
        return studentOldRepository.findById(nationalId).orElse(null); // Return null if not found
    }

    public String getBiometricTagByIdNumber(String idNumber) {
        // Use Optional to find the patient
        Optional<StudentOldModel> optionalPatient = studentOldRepository.findByIdNumber(idNumber);

        // Check if the patient is present and handle accordingly
        StudentOldModel studentOldModel = optionalPatient.orElseThrow(() -> new StudentOldController.PatientNotFoundException("Pensioner not found"));

        // Return the biometric tag from the patient
        return studentOldModel.getBiometric_tag();
    }

    public StudentOldModel getPatientByIdNumber(String idNumber) {
        // Trim and get the first 9 characters
        String trimmedIdNumber = idNumber.trim().substring(0, 9);
        System.out.println("Fetching patient with ID number prefix: " + trimmedIdNumber); // Log the ID number
        return studentOldRepository.findByIdNumberPrefix(trimmedIdNumber)
                .orElseThrow(() -> new StudentOldController.PatientNotFoundException("Patient not found"));
    }





    private void updateNextPaymentDate(StudentOldModel studentOldModel) {
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





    public boolean suspendPatientIfRequired(StudentOldModel studentOldModel) {
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
        StudentOldModel studentOldModel = studentOldRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new StudentOldController.PatientNotFoundException("Pensioner not found"));

        // Set COE date to current date
        LocalDate currentDate = LocalDate.now();

        // Set COE expected date to one year later
        LocalDate expectedDate = currentDate.plusYears(1);

        updateNextPaymentDate(studentOldModel);

        // Save the updated pensioner
        studentOldRepository.save(studentOldModel);
    }

    public PatientPageResponse getPatientsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StudentOldModel> patientPage = studentOldRepository.findAll(pageable);

        return new PatientPageResponse(patientPage.getContent().stream()
                .map(patients -> modelMapper.map(patients, PatientDTO.class))
                .collect(Collectors.toList()),
                patientPage.getTotalElements());
    }

    public PatientPageResponse getPatientsMalePaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Filter by gender where gender is Male
        Page<StudentOldModel> patientPage = studentOldRepository.findAllByGender("Male", pageable);

        return new PatientPageResponse(patientPage.getContent().stream()
                .map(patient -> modelMapper.map(patient, PatientDTO.class))
                .collect(Collectors.toList()),
                patientPage.getTotalElements());
    }

    public PatientPageResponse getPatientsFemalePaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Filter by gender where gender is Male
        Page<StudentOldModel> patientPage = studentOldRepository.findAllByGender("Female", pageable);

        return new PatientPageResponse(patientPage.getContent().stream()
                .map(patient -> modelMapper.map(patient, PatientDTO.class))
                .collect(Collectors.toList()),
                patientPage.getTotalElements());
    }

    public List<StudentOldModel> fetchPatients(String company, String division, String department, String medicalAid) {
        return studentOldRepository.findAll();
    }

    public List<StudentOldModel> findAllPatients() {
        return studentOldRepository.findAll();
    }

    public List<String> getDistinctDepartments() {
        return studentOldRepository.findDistinctDepartments();
    }

    public List<String> getDistinctDivisions() {
        return studentOldRepository.findDistinctDivisions();
    }

    public StudentOldModel getByPersonnelNumber(String personnelNumber) {
        return studentOldRepository.findByPersonnelNumber(personnelNumber)
                .orElseThrow(() -> new StudentOldController.PatientNotFoundException("Patient not found"));
    }

    public void updateEnrollmentStatus(String membership,String enrollmentStatus) {
        Optional<StudentOldModel> byPersonnelNumber = studentOldRepository.findByPersonnelNumber(membership);
        byPersonnelNumber.get().setEnrolmentStatus(enrollmentStatus);
        studentOldRepository.save(byPersonnelNumber.get());
    }

    public void updateStudentEnrollmentStatus(String studentNumber,String enrollmentStatus) {
        Optional<Student> byStudentNumber = studentRepository.findByStudentNumber(studentNumber);
        byStudentNumber.get().setEnrolmentStatus(enrollmentStatus);
        byStudentNumber.get().setIsBiometric(true);
        byStudentNumber.get().setEnrollmentDate(LocalDate.now());
        studentRepository.save(byStudentNumber.get());
    }
    public void updateEnrollmentStatusDependant(String membership,String enrollmentStatus, int suffix) {
        Optional<Dependant> byPersonnelNumberAndSuffix = studentsDepRepository.findByPersonnelNumberAndSuffix(membership,suffix);
        byPersonnelNumberAndSuffix.get().setEnrolmentStatus(enrollmentStatus);
        studentsDepRepository.save(byPersonnelNumberAndSuffix.get());
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
        List<Dependant> dependants = studentsDepRepository.findByPersonnelNumber(personnelNumber);
        return ResponseEntity.ok(new BaseResult(dependants, "Dependants fetched successfully",
                "00"));
    }


    public ResponseEntity<BaseResult> getDependantByPersonnelAndSuffix(String personnelNumber, int suffix) {
        Optional<Dependant> dependants = studentsDepRepository.findByPersonnelNumberAndSuffix(personnelNumber,suffix);
        return ResponseEntity.ok(new BaseResult(dependants.get(), "Dependant fetched successfully",
                "00"));
    }

    public ResponseEntity<BaseResult> getFindAllDependants() {
        List<Dependant> dependants = studentsDepRepository.findAll();
        return ResponseEntity.ok(new BaseResult(dependants, "Dependant fetched successfully",
                "00"));
    }
}