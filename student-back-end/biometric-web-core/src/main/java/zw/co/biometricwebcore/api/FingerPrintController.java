package zw.co.biometricwebcore.api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import zw.co.biometricwebcore.domain.model.AuditTrail;
import zw.co.biometricwebcore.domain.model.PatientFingerPrint;
import zw.co.biometricwebcore.domain.service.AuditService;
import zw.co.biometricwebcore.domain.service.FingerPrintService;

import java.util.List;

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

    @PostMapping("/create")
    public PatientFingerPrint createFingerPrint(@RequestBody PatientFingerPrint patientFingerPrint) {
        System.out.println(patientFingerPrint.getPersonnelNumberPlusSuffix());
        return fingerPrintService.createFingerPrint(patientFingerPrint);
    }






}