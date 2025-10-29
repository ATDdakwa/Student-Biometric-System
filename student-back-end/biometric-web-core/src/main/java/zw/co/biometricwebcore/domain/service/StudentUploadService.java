package zw.co.biometricwebcore.domain.service;

import java.util.List;

public interface StudentUploadService {
    void uploadToTemp(List<String[]> records);
    void syncTempToPatients();

}
