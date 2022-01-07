package account.controller;


import account.entity.User;
import account.remote.request.ChangePasswordRequest;
import account.remote.request.SignupRequest;
import account.remote.response.ChangePasswordResponse;
import account.remote.response.SignupResponse;
import account.response.UserResponse;
import account.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Locale;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationService service;


    public AuthController(AuthenticationService service) {
        this.service = service;
    }


    @PostMapping("/auth/signup")
    public SignupResponse signup(@Valid @RequestBody User user) {
        return service.register(user);
    }




    @PostMapping("/auth/changepass")
    public ChangePasswordResponse changepass(@Valid @RequestBody ChangePasswordRequest request,Principal principal) {
        return service.changepass(request,principal);
    }











}
