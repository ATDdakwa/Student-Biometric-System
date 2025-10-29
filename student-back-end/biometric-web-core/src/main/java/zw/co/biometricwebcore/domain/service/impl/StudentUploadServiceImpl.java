package zw.co.biometricwebcore.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.model.StudentOldModel;
import zw.co.biometricwebcore.domain.model.StudentsTemp;
import zw.co.biometricwebcore.domain.repository.StudentOldRepository;
import zw.co.biometricwebcore.domain.repository.StudentsTempRepository;
import zw.co.biometricwebcore.domain.service.StudentUploadService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentUploadServiceImpl implements StudentUploadService {

    private final StudentOldRepository studentOldRepository;
    private final StudentsTempRepository patientTempRepository;

    @Override
    public void uploadToTemp(List<String[]> records) {
        for (int i = 1; i < records.size(); i++) {
            System.out.println("Param " + i + ": " + Arrays.toString(records.get(i)));

            String[] record = records.get(i);
            StudentsTemp temp = new StudentsTemp();

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
        List<StudentsTemp> tempRecords = patientTempRepository.findAll();

// Fetch all personnel numbers in bulk
        List<String> personnelNumbers = tempRecords.stream()
                .map(StudentsTemp::getPersonnelNumber)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

// Fetch all existing patients in one query
        List<StudentOldModel> existingStudentOldModels = studentOldRepository.findByPersonnelNumberIn(personnelNumbers);

// Create a lookup map for faster matching
        Map<String, StudentOldModel> patientMap = existingStudentOldModels.stream()
                .collect(Collectors.toMap(StudentOldModel::getPersonnelNumber, Function.identity()));

// Counter for matched records, addedCount and updatedCount
        int matchedCount = 0;
        int addedCount = 0;
        int updatedCount = 0;

// Process records
        for (StudentsTemp temporaryPatient : tempRecords) {
            try {
                StudentOldModel existingStudentOldModel = patientMap.get(temporaryPatient.getPersonnelNumber());
                if (existingStudentOldModel != null) {
                    System.out.println("Existing patient: " + existingStudentOldModel);
                    // Matched record found
                    matchedCount++;

                    // Check if any important fields have changed (firstName, surname, status)
                    boolean isUpdated = false;

                    if (!existingStudentOldModel.getFirstName().equals(temporaryPatient.getFirstName())) {
                        existingStudentOldModel.setFirstName(temporaryPatient.getFirstName());
                        isUpdated = true;
                    }
                    if (!existingStudentOldModel.getSection().equals(temporaryPatient.getSection())) {
                        existingStudentOldModel.setSection(temporaryPatient.getSection());
                        isUpdated = true;
                    }
                    if (!existingStudentOldModel.getDivision().equals(temporaryPatient.getDivision())) {
                        existingStudentOldModel.setDivision(temporaryPatient.getDivision());
                        isUpdated = true;
                    }
                    if (!existingStudentOldModel.getDepartment().equals(temporaryPatient.getDepartment())) {
                        existingStudentOldModel.setDepartment(temporaryPatient.getDepartment());
                        isUpdated = true;
                    }

                    if (!existingStudentOldModel.getDob().equals(temporaryPatient.getDob())) {
                        existingStudentOldModel.setDob(temporaryPatient.getDob());
                        isUpdated = true;
                    }
                    if (!existingStudentOldModel.getMaritalStatus().equals(temporaryPatient.getMaritalStatus())) {
                        existingStudentOldModel.setMaritalStatus(temporaryPatient.getMaritalStatus());
                        isUpdated = true;
                    }
                    if (!existingStudentOldModel.getIdNumber().equals(temporaryPatient.getIdNumber())) {
                        existingStudentOldModel.setIdNumber(temporaryPatient.getIdNumber());
                        isUpdated = true;
                    }
                    if (!existingStudentOldModel.getOldage().equals(temporaryPatient.getOldage())) {
                        existingStudentOldModel.setOldage(temporaryPatient.getOldage());
                        isUpdated = true;
                    }
                    if (!existingStudentOldModel.getCompany().equals(temporaryPatient.getCompany())) {
                        existingStudentOldModel.setCompany(temporaryPatient.getCompany());
                        isUpdated = true;
                    }

                    if (!existingStudentOldModel.getSurname().equals(temporaryPatient.getSurname())) {
                        existingStudentOldModel.setSurname(temporaryPatient.getSurname());
                        isUpdated = true;
                    }
                    if (!existingStudentOldModel.getStatus().equals(temporaryPatient.getStatus())) {
                        existingStudentOldModel.setStatus(temporaryPatient.getStatus());
                        isUpdated = true;
                    }
                    if (!existingStudentOldModel.getScheme().equals(temporaryPatient.getScheme())) {
                        existingStudentOldModel.setScheme(temporaryPatient.getScheme());
                        isUpdated = true;
                    }

                    if (isUpdated) {
                        // Update the existing patient record
                        studentOldRepository.save(existingStudentOldModel);
                        updatedCount++;
                        System.out.println("Updated patient: " + existingStudentOldModel);
                    }
                }
                else {
                    System.out.println("No existing patient found for: " + temporaryPatient.getPersonnelNumber());
                    // No match found, add a new patient
                    StudentOldModel newStudentOldModel = new StudentOldModel();
                    newStudentOldModel.setPersonnelNumber(temporaryPatient.getPersonnelNumber());
                    newStudentOldModel.setSection(temporaryPatient.getSection());
                    newStudentOldModel.setDivision(temporaryPatient.getDivision());
                    newStudentOldModel.setDepartment(temporaryPatient.getDepartment());
                    newStudentOldModel.setInitials(temporaryPatient.getInitials());
                    newStudentOldModel.setFirstName(temporaryPatient.getFirstName());
                    newStudentOldModel.setSurname(temporaryPatient.getSurname());
                    newStudentOldModel.setNationality(temporaryPatient.getNationality());
                    newStudentOldModel.setDob(temporaryPatient.getDob());
                    newStudentOldModel.setMaritalStatus(temporaryPatient.getMaritalStatus());
                    newStudentOldModel.setIdNumber(temporaryPatient.getIdNumber());
                    newStudentOldModel.setGender(temporaryPatient.getGender());
                    newStudentOldModel.setStatus(temporaryPatient.getStatus());
                    newStudentOldModel.setOldage(temporaryPatient.getOldage());
                    newStudentOldModel.setCompany(temporaryPatient.getCompany());
                    newStudentOldModel.setScheme(temporaryPatient.getScheme());
                    studentOldRepository.save(newStudentOldModel);
                    addedCount++;
                    System.out.println("Added new patient: " + newStudentOldModel);
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
