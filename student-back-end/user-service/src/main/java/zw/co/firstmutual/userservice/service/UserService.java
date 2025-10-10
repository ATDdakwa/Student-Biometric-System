package zw.co.firstmutual.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zw.co.firstmutual.userservice.domain.model.Role;
import zw.co.firstmutual.userservice.domain.model.User;
import zw.co.firstmutual.userservice.domain.model.UserPrincipal;
import zw.co.firstmutual.userservice.domain.repository.UserRepository;
import zw.co.hcpwebcommons.domain.enums.ExceptionCode;
import zw.co.hcpwebcommons.domain.response.UserResponse;
import zw.co.hcpwebcommons.domain.value.Email;
import zw.co.hcpwebcommons.domain.value.MobileNumber;
import zw.co.hcpwebcommons.domain.value.UserName;
import zw.co.hcpwebcommons.exceptions.*;
import zw.co.hcpwebcommons.security.JwtTokenProvider;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    public UserResponse add(User user) {

        var userNameFromDatabase = Optional.ofNullable(userRepository.findUserByUserName(user.getUserName()));

        if (userNameFromDatabase.isPresent())
            throw new UserNameAlreadyExistsException("user.name " + user.getUserName() + " already.taken", ExceptionCode.USER_ALREADY_EXISTS);

        var pin = user.getPassword();

        user.setPassword(passwordEncoder.encode(pin));

        userRepository.save(user);

//        sendConfirmationEmail(user.getEmail());

        return new UserResponse(user.getUserName(), user.getEmail());
    }

    public void authenticateUser(String pin) {

        var user = getCurrentUser();

        if (!passwordEncoder.matches(pin, user.getPassword())) {
            throw new InvalidUserPinException("invalid.user.password", ExceptionCode.INVALID_USER_PIN);
        }
    }

    public User getCurrentUser() {
        return findUserByPrincipal(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                .orElseThrow(() -> new UserAuthenticationErrorException("current.user.is.not.authenticated.properly", ExceptionCode.USER_AUTHENTICATION_ERROR));

    }

    public Optional<User> findUserByPrincipal(final String principal) {

        if (Email.isEmailValid(principal))
            return userRepository.findUserByEmail(new Email(principal));

        if (UserName.isValidUsername(principal))
            return Optional.ofNullable(userRepository.findUserByUserName(principal));

        return Optional.empty();
    }

    public User changePassword(User user) {
        var userFromDatabase = userRepository.findUserByEmail(user.getEmail());

        if (userFromDatabase.isEmpty())
            throw new UserNotFoundException("User does not exist", ExceptionCode.USER_NOT_FOUND);
        // Carry date created timestamp
        user.setCreatedDate(userFromDatabase.get().getCreatedDate());

        //set new password by user
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public List<UserResponse> getAll() {
        var users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new UsersNotAvailableException("Users not found", ExceptionCode.USERS_UNAVAILABLE);
        }
        return users.stream().map(a -> (new UserResponse(a.getUserName(), a.getEmail()))).toList();
    }

    public User getOne(Long id) {
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with the ID " + id + " does not exist", ExceptionCode.USER_NOT_FOUND);
        }
        return user.get();
    }


    public User findByUserName(String userName) {
        var user = Optional.ofNullable(userRepository.findUserByUserName(userName));
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with the userName " + userName + " not found", ExceptionCode.USER_NOT_FOUND);
        }
        return user.get();
    }

//    public boolean verifyToken(String token) {
//
//        return tokenProvider.validateToken(token);
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findUserByUserName(username);
        user.getRole().toString();
        Set<GrantedAuthority> grantedAuthorities = null;
        try {
            userRepository.findUserByUserName(username);
            if (user == null)
                throw new UsernameNotFoundException("user.name: " + username + " not.found.");

            grantedAuthorities = new HashSet<>();
            for (Role role : user.getRole()) {
                String roleName = "" + role.getName();
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roleName);
                grantedAuthorities.add(grantedAuthority);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        if (user == null) {
            throw new UsernameNotFoundException("user.name: " + username + " not.found.");

        }
        return new UserPrincipal(user, user.getPassword(), grantedAuthorities);
    }

    public Optional<User> findByEmail(Email email) {
        var user = userRepository.findUserByEmail(email);

        if (user.isEmpty())
            throw new EmailAddressNotFoundException("email.address.not.found", ExceptionCode.EMAIL_NOT_FOUND);

        return user;
    }


    public void sendConfirmationEmail(Email email) {

        var newUser = userRepository.findUserByEmail(email).get();


        var text = "Good day" + " " + newUser.getUserName() + " " + newUser.getEmail() + " , you have successfully registered on the NDIL HCP admin dashboard and you are receiving this notification as acknowledgement." +
                "\n If this is not your action kindly ignore this email." + "\n\n Regards.";

        SimpleMailMessage accountCreationEmail = new SimpleMailMessage();

        accountCreationEmail.setTo(newUser.getEmail().value());

        accountCreationEmail.setSubject("Welcome to NDIL Admin Dashboard " + newUser.getUserName() + " " + newUser.getEmail());

        accountCreationEmail.setText(text);

        accountCreationEmail.setFrom("hcp.admin@firstmutual.co.zw");

        emailService.sendEmail(accountCreationEmail);

    }

    public User saveUser(User newUser) {
        var user = userRepository.save(newUser);
        System.out.println("user............"+user);
        return  user;
    }

    public User findMemberByMsisdn(MobileNumber mobileNumber) {
        var user = new User();
//
//        if (user.isEmpty())
//            throw new MobileNumberNotFoundException("mobile.address.not.found", ExceptionCode.EMAIL_NOT_FOUND);

        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}