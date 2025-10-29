package zw.co.firstmutual.userservice.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import zw.co.firstmutual.userservice.domain.dto.user.LoginResponse;
import zw.co.firstmutual.userservice.domain.model.Login;
import zw.co.firstmutual.userservice.service.LoginService;
import zw.co.firstmutual.userservice.service.UserService;
import zw.co.hcpwebcommons.domain.value.login.LoginDto;
import zw.co.hcpwebcommons.security.JwtTokenProvider;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class LoginController {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";

    private final LoginService loginService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

//    @SneakyThrows
//    @PostMapping(value = "/login")
//    @CrossOrigin("*")
//    @ApiOperation("Enables a user to login with e-mail address & password")
//    public ResponseEntity loginWithEmailAndPassword(@RequestBody LoginDto accountCredentials) {
//
//        Authentication authentication = authenticationManager.
//                authenticate(new UsernamePasswordAuthenticationToken(
//                        accountCredentials.userName(),
//                        accountCredentials.password()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = jwtTokenProvider.generateToken(authentication);
//        //Check if the authentication was successful. If it is, then return the details of the user
//
//        ApiResponse response;
//        if (authentication.isAuthenticated()) {
//            var authenticatedUser = userService.findByUserName(accountCredentials.userName());
//
//            // Log user login in database
//            Login login = new Login();
//            login.setUser(authenticatedUser);
//            loginService.add(login);
//
//            response = new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name(),jwt);
//            return ResponseEntity.ok().header(HEADER_STRING, TOKEN_PREFIX + " " + jwt).body(response);
//        } else {
//            response = new ApiResponse(HttpStatus.BAD_REQUEST.value(), "invalid.credentials.provided");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.TEXT_PLAIN).body(response);
//
//        }
//    }


    @SneakyThrows
    @PostMapping(value = "/login")
    @CrossOrigin("*")
    @ApiOperation("Enables a user to login with e-mail address & password")
    public ResponseEntity<LoginResponse> loginWithEmailAndPassword(@RequestBody LoginDto accountCredentials) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        accountCredentials.userName(),
                        accountCredentials.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        if (authentication.isAuthenticated()) {
            var authenticatedUser = userService.findByUserName(accountCredentials.userName());

            // Log user login in the database
            Login login = new Login();
            login.setUser(authenticatedUser);
            loginService.add(login);
            LoginResponse response = new LoginResponse(jwt, login);

            return ResponseEntity.ok().header(HEADER_STRING, TOKEN_PREFIX + " " + jwt).body(response);
        } else {
            LoginResponse response = new LoginResponse( "No Token", new Login());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.TEXT_PLAIN).body(response);
        }
    }
}