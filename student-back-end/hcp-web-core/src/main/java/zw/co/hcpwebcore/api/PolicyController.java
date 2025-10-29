package zw.co.hcpwebcore.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import zw.co.hcpwebcommons.api.ApiResponse;
import zw.co.hcpwebcore.domain.service.PolicyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/policy")
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping("/add")
    public ApiResponse addPolicy(@RequestParam("policy-number") String policyNumber) {
        return new ApiResponse(HttpStatus.CREATED.value(), HttpStatus.CREATED.name(), policyService.addPolicy(policyNumber));
    }

    @PutMapping("/update/status")
    public ApiResponse updatePolicyStatus(@RequestParam("policy-number") String policyNumber) {
        return new ApiResponse(HttpStatus.CREATED.value(), HttpStatus.CREATED.name(), policyService.updatePolicyStatus(policyNumber));
    }

    @PutMapping("/update/dependents")
    public ApiResponse updatePolicyDependents(@RequestParam("policy-number") String policyNumber) {
        return new ApiResponse(HttpStatus.CREATED.value(), HttpStatus.CREATED.name(), policyService.updatePolicyDependents(policyNumber));
    }

    @GetMapping("/get/policyNumber")
    public ApiResponse getPolicy(@RequestParam("policy-number") String policyNumber) {
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), policyService.getPolicy(policyNumber));
    }


    @GetMapping(path = "/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse getALlPolicies() {
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), policyService.getAllPolicies());
    }
}
