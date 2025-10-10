package zw.co.firstmutual.userservice.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import zw.co.firstmutual.userservice.domain.dto.user.UserDto;
import zw.co.firstmutual.userservice.domain.model.Role;
import zw.co.firstmutual.userservice.domain.model.User;
import zw.co.firstmutual.userservice.service.EmailService;
import zw.co.firstmutual.userservice.service.RoleService;
import zw.co.firstmutual.userservice.service.UserService;
import zw.co.hcpwebcommons.api.ApiResponse;
import zw.co.hcpwebcommons.domain.value.Email;
import zw.co.hcpwebcommons.domain.value.login.PinResetDto;
import zw.co.hcpwebcommons.register.CreateUserRequest;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;


    @PostMapping(value = "/signUp/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Sign up a user to the HCP Dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse signUpMobile(@RequestBody CreateUserRequest addUserDto) {
        var user = modelMapper.map(addUserDto, User.class);
        System.out.println("hffhfhfhfhhff..........."+addUserDto);
        // Assign the role of the user

        user.setRole(Collections.singleton(roleService.findByName("USER")));

        user.setActive(true);

        return new ApiResponse(HttpStatus.CREATED.value(), HttpStatus.CREATED.name(), userService.add(user));
    }


    @PostMapping(value = "/signUp/admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Sign up a admin to HCP dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse signUpAdmin(@RequestBody CreateUserRequest addUserDto) {
        var user = modelMapper.map(addUserDto, User.class);

        user.setRole(Collections.singleton(roleService.findByName("ADMIN")));

        user.setActive(true);

        return new ApiResponse(HttpStatus.CREATED.value(), HttpStatus.CREATED.name(), userService.add(user));
    }


    @PutMapping("/reset-password")
    @ApiOperation(value = "Password reset for user.", response = ApiResponse.class)
    public ApiResponse resetUserPin(@RequestBody PinResetDto credentialsDto,
                                    @RequestParam Email email) {

        // Get user by their email address
        var user = userService.findByEmail(email).get();

        // Set the user password to the generated password
        user.setPassword(credentialsDto.pin());

        return new ApiResponse(200, "SUCCESS", userService.changePassword(user));
    }

    @PostMapping(value="/add-user",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addUser(@RequestBody UserDto userDto) {
        System.out.println("I am here................................");
        User newUser = new User();
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setSbu(userDto.getSbu());
        newUser.setUserEmail(userDto.getEmail());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());
        newUser.setUserName(userDto.getUserName());
        newUser.setEmail(new Email(userDto.getEmail()));
        newUser.setPassword(hashedPassword);
        newUser.setUserRole(userDto.getRole());
        newUser.setActive(true);

        User savedUser = userService.saveUser(newUser);

        if (savedUser != null) {
            return ResponseEntity.ok("User added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add user");
        }
    }

    @GetMapping("/get-all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


}