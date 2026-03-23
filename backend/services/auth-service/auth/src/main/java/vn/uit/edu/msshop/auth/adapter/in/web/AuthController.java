package vn.uit.edu.msshop.auth.adapter.in.web;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.auth.adapter.in.web.mapper.AccountMapper;
import vn.uit.edu.msshop.auth.adapter.in.web.request.CreateAccountRequest;
import vn.uit.edu.msshop.auth.application.port.in.CreateAccountUseCase;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final CreateAccountUseCase createUseCase;
    private final AccountMapper mapper;
    @GetMapping
    @PreAuthorize("hasRole('Client User')")
    public String hello(){
        return "Hello from spring boot key cloak";
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('Client Admin')")
    public String helloAdmin() {
        return "Hello from admin";
    }

    
}
