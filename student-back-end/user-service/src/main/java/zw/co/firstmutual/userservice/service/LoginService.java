package zw.co.firstmutual.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zw.co.firstmutual.userservice.domain.model.Login;
import zw.co.firstmutual.userservice.domain.model.User;
import zw.co.firstmutual.userservice.domain.repository.LoginRepository;
import zw.co.firstmutual.userservice.domain.repository.UserRepository;
import zw.co.hcpwebcommons.api.ApiResponse;
import zw.co.hcpwebcommons.domain.enums.ExceptionCode;
import zw.co.hcpwebcommons.domain.value.Email;
import zw.co.hcpwebcommons.exceptions.LoginEntriesUnavailableException;
import zw.co.hcpwebcommons.exceptions.LoginEntryUnavailableException;
import zw.co.hcpwebcommons.exceptions.UserLoginEntriesUnavailableException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    private final LoginRepository loginRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;


    public ApiResponse add(Login login) {

        var existingUser = userService.findByUserName(login.getUser().getUserName());

        //if user doesn't exist, create a new user entry based on the AD
        if (existingUser == null) {

            var newUser = User.builder()
                    .email(login.getUser().getEmail())
//                    .firstName(login.getUser().getFirstName())
//                    .lastName(login.getUser().getLastName())
                    .userName(login.getUser().getUserName())
                    .password(passwordEncoder.encode(login.getUser().getPassword()))        //encode password as a prerequisite
                    .role(Collections.singleton(roleService.findByName("USER")))
                    .isActive(false)    //set active to false by default
                    .build();

            //add user to local db if it exists
            userRepository.save(newUser);

            //send e-mail to admin for new signed up user
            newUserRequestEmail(newUser.getEmail());

            return new ApiResponse(HttpStatus.OK.value(), "New User Request has been submitted successfully");
        }
        //TODO: restrict user from logging in wen they are inactive even wen they exist
        loginRepository.save(login);

        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.name());
    }

    public List<Login> getAll() {
        var loginList = loginRepository.findAll();
        if (loginList.isEmpty()) {
            throw new EntityNotFoundException("No login entries found");
        }
        return loginList;
    }

    public Login getOne(Long id) {
        var login = loginRepository.findById(id);
        if (login.isEmpty()) {
            throw new LoginEntryUnavailableException("Login with ID " + id + " not found", ExceptionCode.LOGIN_ENTRY_UNAVAILABLE);
        }
        return login.get();
    }

    public List<Login> findAllByUser(User user) {
        var loginList = loginRepository.findAllByUser(user);
        if (loginList.isEmpty()) {
            throw new UserLoginEntriesUnavailableException("Login entries by user " + user.getEmail() + " not found", ExceptionCode.USER_LOGIN_ENTRY_UNAVAILABLE);
        }
        return loginList;
    }

    public List<Login> findAllByDate(Date date) {
        List<Login> loginList = loginRepository.findAllByCreatedDate(date);
        if (loginList.isEmpty()) {
            throw new LoginEntriesUnavailableException("No login entries found for the date " + date.toString(), ExceptionCode.LOGIN_ENTRIES_UNVAILABLE);
        }
        return loginList;
    }

    public void newUserRequestEmail(Email email) {

        var member = userService.findByEmail(email).get();


        var text = "Good day,\n Kindly confirm member with the following details;\n\n Full Name: " + member.getUserName() + " " + member.getEmail() +
                "\n Email: " + member.getEmail() + "\n\n Regards.";

        SimpleMailMessage accountCreationEmail = new SimpleMailMessage();

        accountCreationEmail.setTo("e-services@firstmutual.co.zw");

        accountCreationEmail.setSubject("New User Request " + member.getUserName() + " " + member.getEmail());

        accountCreationEmail.setText(text);

        accountCreationEmail.setFrom("hcp.admin@firstmutual.co.zw");

        emailService.sendEmail(accountCreationEmail);
    }
}