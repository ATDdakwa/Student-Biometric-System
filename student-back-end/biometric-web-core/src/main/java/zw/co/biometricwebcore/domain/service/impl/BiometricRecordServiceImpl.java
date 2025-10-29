package zw.co.biometricwebcore.domain.service.impl;

import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.model.BiometricRecord;
import zw.co.biometricwebcore.domain.repository.ImageTemplateRepository;
import zw.co.biometricwebcore.domain.service.BiometricRecordService;

import java.util.List;

@Service
public class BiometricRecordServiceImpl implements BiometricRecordService {
	
	private final ImageTemplateRepository imageTemplateRepository;
	
	

	public BiometricRecordServiceImpl(ImageTemplateRepository imageTemplateRepository) {
		this.imageTemplateRepository = imageTemplateRepository;
	}

	@Override
	public void save(BiometricRecord biometricRecord) {
		imageTemplateRepository.save(biometricRecord);
		
	}

	@Override
	public List<BiometricRecord> findAll() {
		return imageTemplateRepository.findAll();
	}

}
