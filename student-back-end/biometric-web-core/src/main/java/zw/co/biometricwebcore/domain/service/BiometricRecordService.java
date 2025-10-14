package zw.co.biometricwebcore.domain.service;



import zw.co.biometricwebcore.domain.model.BiometricRecord;

import java.util.List;

public interface BiometricRecordService {

	public void save(BiometricRecord biometricRecord);

	public List<BiometricRecord> findAll();
}
