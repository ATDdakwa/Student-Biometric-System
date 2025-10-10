package zw.co.biometricwebcore.domain.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.model.Dependant;
import zw.co.biometricwebcore.domain.model.DependantsTemp;
import zw.co.biometricwebcore.domain.repository.DependantRepository;
import zw.co.biometricwebcore.domain.repository.DependantsTempRepository;
import zw.co.biometricwebcore.domain.service.DependantUploadService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DependantsUploadServiceImpl implements DependantUploadService {

    private final DependantRepository dependantRepository;
    private final DependantsTempRepository dependantTempRepository;


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
            DependantsTemp dependantsTemp = new DependantsTemp();

            dependantsTemp.setPersonnelNumber(getValue(record, 0));
            dependantsTemp.setFullName(getValue(record, 1));
            dependantsTemp.setDob(getValue(record, 2));
            dependantsTemp.setGender(getValue(record, 3));
            dependantsTemp.setSuffix(getIntegerValue(record, 4));
            dependantsTemp.setRelation(getValue(record, 5));
            dependantsTemp.setIdNumber(getValue(record, 6));
            dependantTempRepository.save(dependantsTemp);
        }
    }

    @Override
    public void syncTempToDependants() {
        List<DependantsTemp> tempRecords = dependantTempRepository.findAll();

        // Fetch all unique personnel numbers and suffixes in bulk
        List<String> personnelNumbers = tempRecords.stream()
                .map(DependantsTemp::getPersonnelNumber)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        List<Integer> suffixes = tempRecords.stream()
                .map(DependantsTemp::getSuffix)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());



        // Fetch all existing dependants in one query
        List<Dependant> existingDependants = dependantRepository.findByPersonnelNumberInAndSuffixIn(personnelNumbers, suffixes);

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
        for (DependantsTemp temporaryDependant : tempRecords) {
            try {
                String compositeKey = temporaryDependant.getPersonnelNumber() + "|" + temporaryDependant.getSuffix();
                Dependant existingDependant = dependantMap.get(compositeKey);

                System.out.println("composite key" + compositeKey);
                if (existingDependant != null) {
                    // Matched record found
                    matchedCount++;

                    // Check if any important fields have changed (fullName, dob, relation)
                    boolean isUpdated = false;

                    if (!existingDependant.getFullName().equals(temporaryDependant.getFullName())) {
                        existingDependant.setFullName(temporaryDependant.getFullName());
                        isUpdated = true;
                    }
                    if (!existingDependant.getDob().equals(temporaryDependant.getDob())) {
                        existingDependant.setDob(temporaryDependant.getDob());
                        isUpdated = true;
                    }
                    if (!existingDependant.getRelation().equals(temporaryDependant.getRelation())) {
                        existingDependant.setRelation(temporaryDependant.getRelation());
                        isUpdated = true;
                    }

                    if (isUpdated) {
                        // Update the existing dependant record
                        dependantRepository.save(existingDependant);
                        updatedCount++;
                        System.out.println("Updated dependant: " + existingDependant);
                    }
                } else {
                    // No match found, add a new dependant
                    Dependant newDependant = getDependant(temporaryDependant);
                    dependantRepository.save(newDependant);
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

    private static Dependant getDependant(DependantsTemp temporaryDependant) {
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
