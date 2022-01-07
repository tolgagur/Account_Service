package account.service;

import account.entity.User;
import account.exception.UserExistException;
import account.remote.request.ChangePasswordRequest;
import account.remote.request.SignupRequest;
import account.remote.response.ChangePasswordResponse;
import account.remote.response.SignupResponse;
import account.repo.UserRepository;
import account.exception.SamePasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Locale;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidationService passwordValidationService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordValidationService passwordValidationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordValidationService = passwordValidationService;
    }


    public SignupResponse register(User user) {
        Optional<User> optionalUser = userRepository.findUserByEmailEqualsIgnoreCase(user.getEmail().toLowerCase(Locale.ROOT));
        if (optionalUser.isPresent()) throw new UserExistException();
        passwordValidationService.validate(user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return new SignupResponse(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getEmail().toLowerCase(Locale.ROOT)
        );
    }

    public User findUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findUserByEmailEqualsIgnoreCase(email);
        return optionalUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"böyle bir kuulanıcı yok"));
    }

    public ChangePasswordResponse changepass(ChangePasswordRequest request, Principal principal) {

        User user = userRepository.findByEmail(principal.getName());

        passwordValidationService.validate(request.getNewPassword());

        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword()
        )) {
            throw new SamePasswordException();
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new ChangePasswordResponse(
                user.getEmail().toLowerCase(Locale.ROOT),
                "The password has been updated successfully"
        );
    }


    public static User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || (authentication instanceof AnonymousAuthenticationToken)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User must login!"
            );
        }
        User user = (User) authentication.getPrincipal();
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User must login!"
            );
        }
        return user;
    }










}