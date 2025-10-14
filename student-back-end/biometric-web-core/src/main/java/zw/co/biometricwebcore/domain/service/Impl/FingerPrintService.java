package zw.co.biometricwebcore.domain.service.impl;

import com.mantra.morfinauth.MorfinAuth;
import com.mantra.morfinauth.enums.TemplateFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.model.PatientFingerPrint;
import zw.co.biometricwebcore.domain.repository.FingerPrintRepository;
import zw.co.biometricwebcore.domain.service.BiometricScannerService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FingerPrintService {

    private final FingerPrintRepository fingerPrintRepository;
    private final PatientService patientService;

    public PatientFingerPrint updateFingerPrints(PatientFingerPrint patientFingerPrint) {
        PatientFingerPrint existingPrints = new PatientFingerPrint (); //getMemberFingerPrints(patientFingerPrint.getPersonnelNumberPlusSuffix());
        existingPrints.setFirstFingerImage(patientFingerPrint.getFirstFingerImage());
        existingPrints.setSecondFingerImage(patientFingerPrint.getSecondFingerImage());
        existingPrints.setThirdFingerImage(patientFingerPrint.getThirdFingerImage());

        existingPrints.setFirstFingerPrint(patientFingerPrint.getFirstFingerPrint());
        existingPrints.setSecondFingerPrint(patientFingerPrint.getSecondFingerPrint());
        existingPrints.setThirdFingerPrint(patientFingerPrint.getThirdFingerPrint());
        existingPrints.setEnrolmentStatus(patientFingerPrint.getEnrolmentStatus());
        existingPrints.setPersonnelNumberPlusSuffix(patientFingerPrint.getPersonnelNumberPlusSuffix());

        existingPrints.setFirstFingerTemplate(patientFingerPrint.getFirstFingerTemplate());
        existingPrints.setSecondFingerTemplate(patientFingerPrint.getSecondFingerTemplate());
        existingPrints.setThirdFingerTemplate(patientFingerPrint.getThirdFingerTemplate());
        PatientFingerPrint save = fingerPrintRepository.save(existingPrints);




        String membership = patientFingerPrint.getPersonnelNumberPlusSuffix().replaceAll("-.*", "");

        patientService.updateStudentEnrollmentStatus(membership,patientFingerPrint.getEnrolmentStatus());
        return save;
    }

    public PatientFingerPrint createFingerPrint(PatientFingerPrint patientFingerPrint) {
        PatientFingerPrint save = fingerPrintRepository.save(patientFingerPrint);
        return save;
    }

    public PatientFingerPrint createFingerPrintDependant(PatientFingerPrint patientFingerPrint) {
        PatientFingerPrint existingPrints =  new PatientFingerPrint (); //getMemberFingerPrints(patientFingerPrint.getPersonnelNumberPlusSuffix());
        existingPrints.setFirstFingerImage(patientFingerPrint.getFirstFingerImage());
        existingPrints.setSecondFingerImage(patientFingerPrint.getSecondFingerImage());
        existingPrints.setThirdFingerImage(patientFingerPrint.getThirdFingerImage());

        existingPrints.setFirstFingerPrint(patientFingerPrint.getFirstFingerPrint());
        existingPrints.setSecondFingerPrint(patientFingerPrint.getSecondFingerPrint());
        existingPrints.setThirdFingerPrint(patientFingerPrint.getThirdFingerPrint());
        existingPrints.setEnrolmentStatus(patientFingerPrint.getEnrolmentStatus());

        existingPrints.setFirstFingerTemplate(patientFingerPrint.getFirstFingerTemplate());
        existingPrints.setSecondFingerTemplate(patientFingerPrint.getSecondFingerTemplate());
        existingPrints.setThirdFingerTemplate(patientFingerPrint.getThirdFingerTemplate());
        String membershipWithOutSuffix = patientFingerPrint.getPersonnelNumberPlusSuffix();
        existingPrints.setPersonnelNumberPlusSuffix(membershipWithOutSuffix);
        PatientFingerPrint save = fingerPrintRepository.save(existingPrints);
        String membership = patientFingerPrint.getPersonnelNumberPlusSuffix().replaceAll("-.*", "");
        patientService.updateEnrollmentStatusDependant(membership,patientFingerPrint.getEnrolmentStatus(),patientFingerPrint.getSuffix());
        return save;
    }

    public List<PatientFingerPrint> getAllFingerPrints() {
        return  fingerPrintRepository.findAll();
    }

    public PatientFingerPrint getMemberFingerPrints(String memberNum) {
        return  fingerPrintRepository.findByPersonnelNumberPlusSuffix(memberNum);
    }


}
