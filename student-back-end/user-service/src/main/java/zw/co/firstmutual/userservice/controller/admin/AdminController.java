package zw.co.firstmutual.userservice.controller.admin;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import zw.co.firstmutual.userservice.domain.model.User;
import zw.co.firstmutual.userservice.service.UserService;
import zw.co.hcpwebcommons.api.ApiResponse;
import zw.co.hcpwebcommons.domain.value.Email;
import zw.co.hcpwebcommons.domain.value.MobileNumber;
import zw.co.hcpwebcommons.domain.value.UserName;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping(value = "/api/v1/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@RolesAllowed("ADMIN")
public class AdminController {

    private final UserService userService;

    @GetMapping("/")
    public ApiResponse getAllUsers() {
        return new ApiResponse(HttpStatus.OK.value(), "SUCCESS", userService.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse getOneUser(@PathVariable("id") Long id) {
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), userService.getOne(id));
    }

    @GetMapping("/email")
    public ApiResponse findByEmail(@RequestParam("email") Email email) {
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), userService.findByEmail(email));
    }

    @GetMapping("/mobile_number")
    public ApiResponse findMemberByMsisdn(@RequestParam("mobileNumber") MobileNumber mobileNumber) {
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), userService.findMemberByMsisdn(mobileNumber));
    }

    @GetMapping("/user-name")
    public ApiResponse findByUserName(@RequestParam("username") String userName) {
        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(), userService.findByUserName(userName));
    }


    @GetMapping("/current")
    @ApiOperation(value = "Get currently logged user", response = ApiResponse.class)
    public User getCurrentActualUser() {
        return userService.getCurrentUser();
    }

//    @GetMapping("/verify-token")
//    ApiResponse verifyToken(@RequestParam("token") String token) {
//        return new ApiResponse(200, "SUCCESS", userService.verifyToken(token));
//    }

}