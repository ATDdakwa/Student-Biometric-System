package zw.co.biometricwebcore.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.model.Dependant;
import zw.co.biometricwebcore.domain.model.StudentsDepTemp;
import zw.co.biometricwebcore.domain.repository.StudentsDepRepository;
import zw.co.biometricwebcore.domain.repository.StudentsDepTempRepository;
import zw.co.biometricwebcore.domain.service.StudentDepUploadService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentDepUploadServiceImpl implements StudentDepUploadService {

    private final StudentsDepRepository studentsDepRepository;
    private final StudentsDepTempRepository dependantTempRepository;


    private String getValue(String[] record, int index) {
        return (record.length > index) ? record[index] : "";
    }

    private Integer getIntegerValue(String[] record, int index) {
        try {
            return (record.length > index && !record[index].isEmpty()) ? Integer.parseInt(record[index]) : null;
        } catch (NumberFormatException e) {
            System.out.println("Failed to parse integer at index " + index + ": " + record[index]);
            return null;
        }
    }

    @Override
    public void uploadToTemp(List<String[]> records) {
        // Start processing from the second row (index 1)
        for (int i = 0; i < records.size(); i++) {
            System.out.println("Param " + i + ": " + Arrays.toString(records.get(i)));

            String[] record = records.get(i);
            StudentsDepTemp studentsDepTemp = new StudentsDepTemp();

            studentsDepTemp.setPersonnelNumber(getValue(record, 0));
            studentsDepTemp.setFullName(getValue(record, 1));
            studentsDepTemp.setDob(getValue(record, 2));
            studentsDepTemp.setGender(getValue(record, 3));
            studentsDepTemp.setRelation(getValue(record, 4));
            studentsDepTemp.setIdNumber(getValue(record, 5));
            studentsDepTemp.setSuffix(getIntegerValue(record, 6));

            dependantTempRepository.save(studentsDepTemp);
        }
    }

    @Override
    public void syncTempToDependants() {
        List<StudentsDepTemp> tempRecords = dependantTempRepository.findAll();

        // Fetch all unique personnel numbers and suffixes in bulk
        List<String> personnelNumbers = tempRecords.stream()
                .map(StudentsDepTemp::getPersonnelNumber)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        List<Integer> suffixes = tempRecords.stream()
                .map(StudentsDepTemp::getSuffix)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());



        // Fetch all existing dependants in one query
        List<Dependant> existingDependants = studentsDepRepository.findByPersonnelNumberInAndSuffixIn(personnelNumbers, suffixes);

        // Create a lookup map with a composite key (personnelNumber + suffix)
        Map<String, Dependant> dependantMap = existingDependants.stream()
                .collect(Collectors.toMap(
                        d -> d.getPersonnelNumber() + "|" + d.getSuffix(),
                        Function.identity()
                ));

        // Counters for matched, added, and updated records
        int matchedCount = 0;
        int addedCount = 0;
        int updatedCount = 0;

        // Process records
        for (StudentsDepTemp temporaryDependant : tempRecords) {
            try {
                String compositeKey = temporaryDependant.getPersonnelNumber() + "|" + temporaryDependant.getSuffix();
                Dependant existingDependant = dependantMap.get(compositeKey);

                System.out.println("composite key" + compositeKey);
                if (existingDependant != null) {
                    // Matched record found
                    matchedCount++;

                    // Check if any important fields have changed (fullName, dob, idNumber and relation)
                    boolean isUpdated = false;

                    if (!existingDependant.getFullName().equals(temporaryDependant.getFullName())) {
                        existingDependant.setFullName(temporaryDependant.getFullName());
                        isUpdated = true;
                    }
                    if (!existingDependant.getDob().equals(temporaryDependant.getDob())) {
                        existingDependant.setDob(temporaryDependant.getDob());
                        isUpdated = true;
                    }
                    if (!existingDependant.getIdNumber().equals(temporaryDependant.getIdNumber())) {
                        existingDependant.setIdNumber(temporaryDependant.getIdNumber());
                        isUpdated = true;
                    }
                    if (!existingDependant.getRelation().equals(temporaryDependant.getRelation())) {
                        existingDependant.setRelation(temporaryDependant.getRelation());
                        isUpdated = true;
                    }

                    if (isUpdated) {
                        // Update the existing dependant record
                        studentsDepRepository.save(existingDependant);
                        updatedCount++;
                        System.out.println("Updated dependant: " + existingDependant);
                    }
                } else {
                    // No match found, add a new dependant
                    Dependant newDependant = getDependant(temporaryDependant);
                    studentsDepRepository.save(newDependant);
                    addedCount++;
                    System.out.println("Added new dependant: " + newDependant);
                }
            } catch (Exception e) {
                System.out.println("Error processing dependant: " + temporaryDependant.getId() + " - " + e.getMessage());
            }
        }

        // Print the total count of matched, added, and updated records
        System.out.println("Total existing dependants matched: " + matchedCount);
        System.out.println("Total new dependants added: " + addedCount);
        System.out.println("Total dependants updated: " + updatedCount);

        dependantTempRepository.truncateTable();
        System.out.println("Table dependants_temp truncated successfully.");
    }

    private static Dependant getDependant(StudentsDepTemp temporaryDependant) {
        Dependant newDependant = new Dependant();
        newDependant.setPersonnelNumber(temporaryDependant.getPersonnelNumber());
        newDependant.setFullName(temporaryDependant.getFullName());
        newDependant.setDob(temporaryDependant.getDob());
        newDependant.setIdNumber(temporaryDependant.getIdNumber());
        newDependant.setGender(temporaryDependant.getGender());
        newDependant.setSuffix(temporaryDependant.getSuffix());
        newDependant.setRelation(temporaryDependant.getRelation());
        return newDependant;
    }
}
