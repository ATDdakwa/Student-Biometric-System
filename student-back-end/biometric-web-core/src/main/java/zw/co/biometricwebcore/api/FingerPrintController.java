package zw.co.biometricwebcore.api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import zw.co.biometricwebcore.domain.model.PatientFingerPrint;
import zw.co.biometricwebcore.domain.service.BiometricScannerService;
import zw.co.biometricwebcore.domain.service.impl.FingerPrintService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fingerprints")
public class FingerPrintController {

    private final FingerPrintService fingerPrintService;


    @GetMapping(path = "/membership/{memberNum}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all fingerprints details with pagination")
    public PatientFingerPrint getAllFingerPrints(@PathVariable String memberNum) {
        return fingerPrintService.getMemberFingerPrints(memberNum);
    }

    @PostMapping("/update")
    public PatientFingerPrint createFingerPrint(@RequestBody PatientFingerPrint patientFingerPrint) {
        System.out.println(patientFingerPrint.getPersonnelNumberPlusSuffix());
        return fingerPrintService.updateFingerPrints(patientFingerPrint);
    }

    @PostMapping("/create-for-dependant")
    public PatientFingerPrint createFingerPrintDependant(@RequestBody PatientFingerPrint patientFingerPrint) {
        System.out.println(patientFingerPrint.getPersonnelNumberPlusSuffix());
        return fingerPrintService.createFingerPrintDependant(patientFingerPrint);
    }






}