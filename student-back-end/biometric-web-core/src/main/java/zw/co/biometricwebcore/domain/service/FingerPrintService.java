package zw.co.biometricwebcore.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.model.AuditTrail;
import zw.co.biometricwebcore.domain.model.PatientFingerPrint;
import zw.co.biometricwebcore.domain.repository.AuditRepository;
import zw.co.biometricwebcore.domain.repository.FingerPrintRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FingerPrintService {

    private final FingerPrintRepository fingerPrintRepository;
    public PatientFingerPrint createFingerPrint(PatientFingerPrint patientFingerPrint) {
        return fingerPrintRepository.save(patientFingerPrint);
    }

    public List<PatientFingerPrint> getAllFingerPrints() {
        return  fingerPrintRepository.findAll();
    }

    public PatientFingerPrint getMemberFingerPrints(String memberNum) {
        return  fingerPrintRepository.findByPersonnelNumberPlusSuffix(memberNum);
    }
}
