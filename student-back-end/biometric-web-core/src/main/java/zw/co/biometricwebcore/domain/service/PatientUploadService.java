package zw.co.biometricwebcore.domain.service;

import zw.co.biometricwebcore.domain.model.Patient;
import zw.co.biometricwebcore.domain.model.PatientsTemp;

import java.util.List;

public interface PatientUploadService {
    void uploadToTemp(List<String[]> records);
    void syncTempToPatients();

}
