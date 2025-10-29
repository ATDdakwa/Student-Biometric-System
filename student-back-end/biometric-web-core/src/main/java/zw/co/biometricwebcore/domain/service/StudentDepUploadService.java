package zw.co.biometricwebcore.domain.service;

import java.util.List;

public interface StudentDepUploadService {

    void uploadToTemp(List<String[]> records);
    void syncTempToDependants();
}
