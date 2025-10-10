package zw.co.biometricwebcore.api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.biometricwebcore.domain.model.AuditTrail;
import zw.co.biometricwebcore.domain.model.Patient;
import zw.co.biometricwebcore.domain.service.AuditService;
import zw.co.biometricwebcore.domain.service.PatientService;
import zw.co.biometricwebcore.domain.service.SMSService;
import zw.co.biometricwebcore.request.PatientDTO;
import zw.co.biometricwebcore.request.TextRequest;
import zw.co.biometricwebcore.response.BaseResult;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/audit")
public class AuditController {

    private final AuditService auditService;

    @GetMapping(path = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all logs details with pagination")
    public List<AuditTrail> getAllLogs() {
        return auditService.getAllLogs();
    }

    @PostMapping("/create")
    public AuditTrail createTrail(@RequestBody AuditTrail auditTrail) {
        return auditService.createTrail(auditTrail);
    }






}