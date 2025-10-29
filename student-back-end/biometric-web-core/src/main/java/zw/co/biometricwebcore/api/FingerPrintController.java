package zw.co.biometricwebcore.api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import zw.co.biometricwebcore.domain.model.StudentFingerPrint;
import zw.co.biometricwebcore.domain.service.impl.FingerPrintService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fingerprints")
public class FingerPrintController {

    private final FingerPrintService fingerPrintService;


    @GetMapping(path = "/membership/{memberNum}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all fingerprints details with pagination")
    public StudentFingerPrint getAllFingerPrints(@PathVariable String memberNum) {
        return fingerPrintService.getMemberFingerPrints(memberNum);
    }

    @PostMapping("/update")
    public StudentFingerPrint createFingerPrint(@RequestBody StudentFingerPrint studentFingerPrint) {
        System.out.println(studentFingerPrint.getPersonnelNumberPlusSuffix());
        return fingerPrintService.updateFingerPrints(studentFingerPrint);
    }

    @PostMapping("/create-for-dependant")
    public StudentFingerPrint createFingerPrintDependant(@RequestBody StudentFingerPrint studentFingerPrint) {
        System.out.println(studentFingerPrint.getPersonnelNumberPlusSuffix());
        return fingerPrintService.createFingerPrintDependant(studentFingerPrint);
    }






}