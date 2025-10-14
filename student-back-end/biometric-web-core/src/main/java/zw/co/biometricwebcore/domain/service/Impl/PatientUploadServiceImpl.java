package zw.co.biometricwebcore.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.model.Patient;
import zw.co.biometricwebcore.domain.model.PatientsTemp;
import zw.co.biometricwebcore.domain.repository.PatientRepository;
import zw.co.biometricwebcore.domain.repository.PatientsTempRepository;
import zw.co.biometricwebcore.domain.service.PatientUploadService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PatientUploadServiceImpl implements PatientUploadService {

    private final PatientRepository patientRepository;
    private final PatientsTempRepository patientTempRepository;

    @Override
    public void uploadToTemp(List<String[]> records) {
        for (int i = 1; i < records.size(); i++) {
            System.out.println("Param " + i + ": " + Arrays.toString(records.get(i)));

            String[] record = records.get(i);
            PatientsTemp temp = new PatientsTemp();

            temp.setPersonnelNumber(getValue(record, 0));
            temp.setSection(getValue(record, 1));
            temp.setDivision(getValue(record, 2));
            temp.setDepartment(getValue(record, 3));
            temp.setInitials(getValue(record, 4));
            temp.setFirstName(getValue(record, 5));
            temp.setSurname(getValue(record, 6));
            temp.setNationality(getValue(record, 7));
            temp.setDob(getValue(record, 8));
            temp.setMaritalStatus(getValue(record, 9));
            temp.setIdNumber(getValue(record, 10));
            temp.setGender(getValue(record, 11));
            temp.setStatus(getValue(record, 12));
            temp.setOldage(getValue(record, 13));
            temp.setCompany(getValue(record, 14));
            temp.setScheme(getValue(record, 15));
            temp.setEmail(getValue(record, 16));

            patientTempRepository.save(temp);
        }
    }

    private String getValue(String[] record, int index) {
        return (record.length > index) ? record[index] : "";
    }

    @Override
    public void syncTempToPatients() {
        List<PatientsTemp> tempRecords = patientTempRepository.findAll();

// Fetch all personnel numbers in bulk
        List<String> personnelNumbers = tempRecords.stream()
                .map(PatientsTemp::getPersonnelNumber)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

// Fetch all existing patients in one query
        List<Patient> existingPatients = patientRepository.findByPersonnelNumberIn(personnelNumbers);

// Create a lookup map for faster matching
        Map<String, Patient> patientMap = existingPatients.stream()
                .collect(Collectors.toMap(Patient::getPersonnelNumber, Function.identity()));

// Counter for matched records, addedCount and updatedCount
        int matchedCount = 0;
        int addedCount = 0;
        int updatedCount = 0;

// Process records
        for (PatientsTemp temporaryPatient : tempRecords) {
            try {
                Patient existingPatient = patientMap.get(temporaryPatient.getPersonnelNumber());
                if (existingPatient != null) {
                    System.out.println("Existing patient: " + existingPatient);
                    // Matched record found
                    matchedCount++;

                    // Check if any important fields have changed (firstName, surname, status)
                    boolean isUpdated = false;

                    if (!existingPatient.getFirstName().equals(temporaryPatient.getFirstName())) {
                        existingPatient.setFirstName(temporaryPatient.getFirstName());
                        isUpdated = true;
                    }
                    if (!existingPatient.getSection().equals(temporaryPatient.getSection())) {
                        existingPatient.setSection(temporaryPatient.getSection());
                        isUpdated = true;
                    }
                    if (!existingPatient.getDivision().equals(temporaryPatient.getDivision())) {
                        existingPatient.setDivision(temporaryPatient.getDivision());
                        isUpdated = true;
                    }
                    if (!existingPatient.getDepartment().equals(temporaryPatient.getDepartment())) {
                        existingPatient.setDepartment(temporaryPatient.getDepartment());
                        isUpdated = true;
                    }

                    if (!existingPatient.getDob().equals(temporaryPatient.getDob())) {
                        existingPatient.setDob(temporaryPatient.getDob());
                        isUpdated = true;
                    }
                    if (!existingPatient.getMaritalStatus().equals(temporaryPatient.getMaritalStatus())) {
                        existingPatient.setMaritalStatus(temporaryPatient.getMaritalStatus());
                        isUpdated = true;
                    }
                    if (!existingPatient.getIdNumber().equals(temporaryPatient.getIdNumber())) {
                        existingPatient.setIdNumber(temporaryPatient.getIdNumber());
                        isUpdated = true;
                    }
                    if (!existingPatient.getOldage().equals(temporaryPatient.getOldage())) {
                        existingPatient.setOldage(temporaryPatient.getOldage());
                        isUpdated = true;
                    }
                    if (!existingPatient.getCompany().equals(temporaryPatient.getCompany())) {
                        existingPatient.setCompany(temporaryPatient.getCompany());
                        isUpdated = true;
                    }

                    if (!existingPatient.getSurname().equals(temporaryPatient.getSurname())) {
                        existingPatient.setSurname(temporaryPatient.getSurname());
                        isUpdated = true;
                    }
                    if (!existingPatient.getStatus().equals(temporaryPatient.getStatus())) {
                        existingPatient.setStatus(temporaryPatient.getStatus());
                        isUpdated = true;
                    }
                    if (!existingPatient.getScheme().equals(temporaryPatient.getScheme())) {
                        existingPatient.setScheme(temporaryPatient.getScheme());
                        isUpdated = true;
                    }

                    if (isUpdated) {
                        // Update the existing patient record
                        patientRepository.save(existingPatient);
                        updatedCount++;
                        System.out.println("Updated patient: " + existingPatient);
                    }
                }
                else {
                    System.out.println("No existing patient found for: " + temporaryPatient.getPersonnelNumber());
                    // No match found, add a new patient
                    Patient newPatient = new Patient();
                    newPatient.setPersonnelNumber(temporaryPatient.getPersonnelNumber());
                    newPatient.setSection(temporaryPatient.getSection());
                    newPatient.setDivision(temporaryPatient.getDivision());
                    newPatient.setDepartment(temporaryPatient.getDepartment());
                    newPatient.setInitials(temporaryPatient.getInitials());
                    newPatient.setFirstName(temporaryPatient.getFirstName());
                    newPatient.setSurname(temporaryPatient.getSurname());
                    newPatient.setNationality(temporaryPatient.getNationality());
                    newPatient.setDob(temporaryPatient.getDob());
                    newPatient.setMaritalStatus(temporaryPatient.getMaritalStatus());
                    newPatient.setIdNumber(temporaryPatient.getIdNumber());
                    newPatient.setGender(temporaryPatient.getGender());
                    newPatient.setStatus(temporaryPatient.getStatus());
                    newPatient.setOldage(temporaryPatient.getOldage());
                    newPatient.setCompany(temporaryPatient.getCompany());
                    newPatient.setScheme(temporaryPatient.getScheme());
                    patientRepository.save(newPatient);
                    addedCount++;
                    System.out.println("Added new patient: " + newPatient);
                }
            } catch (Exception e) {
                System.out.println("Error processing patient: " + temporaryPatient.getId() + " - " + e.getMessage());
            }
        }
        // Print the total count of matched, added, and updated records
        System.out.println("Total existing patients matched: " + matchedCount);
        System.out.println("Total new patients added: " + addedCount);
        System.out.println("Total patients updated: " + updatedCount);

        patientTempRepository.truncateTable();
        System.out.println("Table patients_temp truncated successfully.");
    }

}
