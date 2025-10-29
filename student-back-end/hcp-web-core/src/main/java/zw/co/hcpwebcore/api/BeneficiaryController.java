package zw.co.hcpwebcore.api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import zw.co.hcpwebcommons.api.ApiResponse;
import zw.co.hcpwebcommons.domain.value.IdNumber;
import zw.co.hcpwebcore.domain.service.BeneficiaryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/beneficiary")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;


    @PutMapping("/edit/deactivate")
    @ApiOperation(value = "Deactivate existing beneficiary")
    public ApiResponse deactivateBeneficiaryFromPolicy(@RequestParam("id-number") String idNumber) {
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), beneficiaryService.deactivateBeneficiary(new IdNumber(idNumber)));
    }


    @PutMapping("/edit/activate")
    @ApiOperation(value = "Activate existing beneficiary")
    public ApiResponse activateBeneficiaryFromPolicy(@RequestParam("id-number") String idNumber) {
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), beneficiaryService.activateBeneficiary(new IdNumber(idNumber)));
    }

    @GetMapping("/get/all/policyNumber")
    @ApiOperation(value = "Get beneficiaries by policy number")
    public ApiResponse getBeneficiariesByPolicyNumber(@RequestParam("policy-number") String policyNumber) {
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), beneficiaryService.findAllBeneficiariesByPolicyNumber(policyNumber));
    }

    @GetMapping("/get/all/membership")
    @ApiOperation(value = "Get beneficiaries by membership")
    public ApiResponse getBeneficiariesByMemberShip(@RequestParam("memberId") long memberId) {
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), beneficiaryService.findAllBeneficiariesByMembership(memberId));
    }


    @GetMapping("/get/nationalId")
    @ApiOperation(value = "Get beneficiary by national Id number")
    public ApiResponse getBeneficiaryByIdentityNumber(@RequestParam("id-number") String idNumber) {
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), beneficiaryService.findBeneficiaryByNationalId(new IdNumber(idNumber)));
    }

    @GetMapping(path = "/" , produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all beneficiaries")
    public ApiResponse getAllBeneficiaries() {
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), beneficiaryService.findAllBeneficiaries());
    }

    @GetMapping("/get/active")
    @ApiOperation(value = "Get all active beneficiaries")
    public ApiResponse getAllActiveBeneficiaries(@RequestParam("isActive") boolean isActive) {
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), beneficiaryService.findALlActiveBeneficiaries(isActive));
    }

}
