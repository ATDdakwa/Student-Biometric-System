package zw.co.biometricwebcore.api;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import zw.co.biometricwebcore.domain.service.DependantUploadService;

import zw.co.biometricwebcore.domain.service.PatientUploadService;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileUploadController {

    private final PatientUploadService patientUploadService;
    private final DependantUploadService dependantUploadService;
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        if (file.isEmpty()) {
            response.put("statusCode", "01");  // Custom success code
            response.put("message", "Please select a file to upload.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            String fileType = file.getOriginalFilename();
            if (fileType == null || (!fileType.endsWith("patients.csv") && !fileType.endsWith("dependants.csv"))) {
                response.put("statusCode", "01");  // Custom success code
                response.put("message", "Please upload either 'patients.csv' or 'dependants.csv'");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            List<String[]> records = parseCsv(file);

            System.out.println(records);
            if (fileType.endsWith("patients.csv")) {
                patientUploadService.uploadToTemp(records);
            } else if (fileType.endsWith("dependants.csv")) {
                 dependantUploadService.uploadToTemp(records);
            }


            response.put("statusCode", "00");  // Custom success code
            response.put("message", "File uploaded and processed successfully.");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("statusCode", "02");  // Custom success code
            response.put("message", "Failed to process the file.'");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/syncTempToPatients")
    public ResponseEntity<Map<String, String>> syncTempToPatients() {
        patientUploadService.syncTempToPatients();
        // Create a response map with custom success code and message
        Map<String, String> response = new HashMap<>();
        response.put("statusCode", "00");  // Custom success code
        response.put("message", "Data synced and processed successfully.");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/syncTempToDependants")
    public ResponseEntity<Map<String, String>> syncTempToDependants() {
        dependantUploadService.syncTempToDependants();
        // Create a response map with custom success code and message
        Map<String, String> response = new HashMap<>();
        response.put("statusCode", "00");  // Custom success code
        response.put("message", "Data synced and processed successfully.");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private List<String[]> oldMethodOfParsingCsv(MultipartFile file) throws IOException {
        System.out.println(file);
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                records.add(line.split(","));
            }
        }
        return records;
    }


    private List<String[]> parseCsv(MultipartFile file) throws IOException {
        List<String[]> records = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                records.add(values);
            }
        } catch (CsvValidationException e) {
            throw new IOException("Failed to validate CSV format", e);
        }
        return records;
    }
}