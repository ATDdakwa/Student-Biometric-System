package zw.co.hcpwebcore.api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.hcpwebcommons.api.ApiResponse;
import zw.co.hcpwebcommons.domain.value.IdNumber;
import zw.co.hcpwebcommons.domain.value.MobileNumber;
import zw.co.hcpwebcore.request.MemberDto;
import zw.co.hcpwebcore.domain.model.Member;
import zw.co.hcpwebcore.domain.service.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;


//    @PutMapping("/edit/policyNumber")
//    @ApiOperation(value = "Add policy Number to user")
//    public ApiResponse addPolicyNumberToMember(@RequestBody UpdateMemberDto updateMemberDto){
//        return   new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),  memberService.updateMember(updateMemberDto));
//    }
    @PutMapping("/edit/policyNumber")
    @ApiOperation(value = "Add policy Number to user")
    public String addPolicyNumberToMember(@RequestBody MemberDto memberDto){
        return   memberService.updatePolicyNumber(memberDto);
    }
    @PutMapping("/edit/deactivate")
    @ApiOperation(value = "Deactivate existing member")
    public ApiResponse deactivateMemberPolicy(@RequestParam("policy-number") String policyNumber){
        return   new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),  memberService.deactivatePolicy(policyNumber));
    }

    @PutMapping("/edit/activate")
    @ApiOperation(value = "Activate existing member")
    public ApiResponse activateMemberPolicy(@RequestParam("policy-number") String policyNumber){
        return   new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),  memberService.activatePolicy(policyNumber));
    }

    @GetMapping("/get/policyNumber")
    @ApiOperation(value = "Get member by policy number")
    public ApiResponse getMemberByPolicyNumber(@RequestParam("policy-number") String policyNumber){
      return   new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),  memberService.findMemberByPolicyNumber(policyNumber));
    }


    @GetMapping("/get/mobile")
    @ApiOperation(value = "Get member by msisdn")
    public ApiResponse getMemberByMsisdn(@RequestParam("msisdn") String msisdn){
        return   new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),  memberService.findMemberByMsisdn(new MobileNumber(msisdn)));
    }

    @GetMapping("/get/nationalId")
    @ApiOperation(value = "Get member by national Id number")
    public ApiResponse getMemberByIdentityNumber(@RequestParam("id-number") String idNumber){
        return   new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),  memberService.findMemberNationalId(new IdNumber(idNumber)));
    }

//    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "Get all members")
//    public ApiResponse getAllMembers(){
//        return   new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),  memberService.findALlMembers());
//    }



    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get all members")
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.findAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/get/active")
    @ApiOperation(value = "Get all active members")
    public ApiResponse getAllActiveMembers(@RequestParam("isActive") boolean isActive){
        return   new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),  memberService.findALlActiveMembers(isActive));
    }

}
