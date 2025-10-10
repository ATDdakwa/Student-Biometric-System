package zw.co.biometricwebcore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zw.co.biometricwebcore.domain.model.AccessPoint;
import zw.co.biometricwebcore.domain.repository.AccessPointRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessPointService {

    private final AccessPointRepository accessPointRepository;

    public List<AccessPoint> findAll() {
        return accessPointRepository.findAll();
    }

    public AccessPoint getById(Long id) {
        return accessPointRepository.findById(id).orElseThrow();
    }

    @Transactional
    public AccessPoint create(AccessPoint ap) {
        return accessPointRepository.save(ap);
    }

    @Transactional
    public AccessPoint update(Long id, AccessPoint updated) {
        AccessPoint existing = getById(id);
        existing.setCode(updated.getCode());
        existing.setName(updated.getName());
        existing.setLocation(updated.getLocation());
        existing.setActive(updated.getActive());
        return accessPointRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        accessPointRepository.deleteById(id);
    }
}
