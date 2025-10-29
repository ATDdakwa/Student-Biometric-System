package zw.co.biometricwebcore.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zw.co.biometricwebcore.domain.model.StudentFingerPrint;
import zw.co.biometricwebcore.domain.repository.FingerPrintRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FingerPrintService {

    private final FingerPrintRepository fingerPrintRepository;
    private final StudentOldService studentOldService;

    public StudentFingerPrint updateFingerPrints(StudentFingerPrint studentFingerPrint) {
        StudentFingerPrint existingPrints = new StudentFingerPrint(); //getMemberFingerPrints(patientFingerPrint.getPersonnelNumberPlusSuffix());
        existingPrints.setFirstFingerImage(studentFingerPrint.getFirstFingerImage());
        existingPrints.setSecondFingerImage(studentFingerPrint.getSecondFingerImage());
        existingPrints.setThirdFingerImage(studentFingerPrint.getThirdFingerImage());

        existingPrints.setFirstFingerPrint(studentFingerPrint.getFirstFingerPrint());
        existingPrints.setSecondFingerPrint(studentFingerPrint.getSecondFingerPrint());
        existingPrints.setThirdFingerPrint(studentFingerPrint.getThirdFingerPrint());
        existingPrints.setEnrolmentStatus(studentFingerPrint.getEnrolmentStatus());
        existingPrints.setPersonnelNumberPlusSuffix(studentFingerPrint.getPersonnelNumberPlusSuffix());

        existingPrints.setFirstFingerTemplate(studentFingerPrint.getFirstFingerTemplate());
        existingPrints.setSecondFingerTemplate(studentFingerPrint.getSecondFingerTemplate());
        existingPrints.setThirdFingerTemplate(studentFingerPrint.getThirdFingerTemplate());
        StudentFingerPrint save = fingerPrintRepository.save(existingPrints);




        String membership = studentFingerPrint.getPersonnelNumberPlusSuffix().replaceAll("-.*", "");

        studentOldService.updateStudentEnrollmentStatus(membership, studentFingerPrint.getEnrolmentStatus());
        return save;
    }

    public StudentFingerPrint createFingerPrint(StudentFingerPrint studentFingerPrint) {
        StudentFingerPrint save = fingerPrintRepository.save(studentFingerPrint);
        return save;
    }

    public StudentFingerPrint createFingerPrintDependant(StudentFingerPrint studentFingerPrint) {
        StudentFingerPrint existingPrints =  new StudentFingerPrint(); //getMemberFingerPrints(patientFingerPrint.getPersonnelNumberPlusSuffix());
        existingPrints.setFirstFingerImage(studentFingerPrint.getFirstFingerImage());
        existingPrints.setSecondFingerImage(studentFingerPrint.getSecondFingerImage());
        existingPrints.setThirdFingerImage(studentFingerPrint.getThirdFingerImage());

        existingPrints.setFirstFingerPrint(studentFingerPrint.getFirstFingerPrint());
        existingPrints.setSecondFingerPrint(studentFingerPrint.getSecondFingerPrint());
        existingPrints.setThirdFingerPrint(studentFingerPrint.getThirdFingerPrint());
        existingPrints.setEnrolmentStatus(studentFingerPrint.getEnrolmentStatus());

        existingPrints.setFirstFingerTemplate(studentFingerPrint.getFirstFingerTemplate());
        existingPrints.setSecondFingerTemplate(studentFingerPrint.getSecondFingerTemplate());
        existingPrints.setThirdFingerTemplate(studentFingerPrint.getThirdFingerTemplate());
        String membershipWithOutSuffix = studentFingerPrint.getPersonnelNumberPlusSuffix();
        existingPrints.setPersonnelNumberPlusSuffix(membershipWithOutSuffix);
        StudentFingerPrint save = fingerPrintRepository.save(existingPrints);
        String membership = studentFingerPrint.getPersonnelNumberPlusSuffix().replaceAll("-.*", "");
        studentOldService.updateEnrollmentStatusDependant(membership, studentFingerPrint.getEnrolmentStatus(), studentFingerPrint.getSuffix());
        return save;
    }

    public List<StudentFingerPrint> getAllFingerPrints() {
        return  fingerPrintRepository.findAll();
    }

    public StudentFingerPrint getMemberFingerPrints(String memberNum) {
        return  fingerPrintRepository.findByPersonnelNumberPlusSuffix(memberNum);
    }


}
