package zw.co.biometricwebcore.domain.repository;

import org.springframework.data.repository.Repository;
import zw.co.biometricwebcore.domain.model.BiometricRecord;

import java.util.List;

@org.springframework.stereotype.Repository
public interface ImageTemplateRepository extends Repository<BiometricRecord, Long>{
	
	 List<BiometricRecord> findAll();
	 void  save(BiometricRecord biometricRecord);

}
