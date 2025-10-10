package zw.co.biometricwebcore.domain.service;

import zw.co.biometricwebcore.domain.model.Dependant;
import zw.co.biometricwebcore.domain.model.DependantsTemp;

import java.util.List;

public interface DependantUploadService {

    void uploadToTemp(List<String[]> records);
    void syncTempToDependants();
}
