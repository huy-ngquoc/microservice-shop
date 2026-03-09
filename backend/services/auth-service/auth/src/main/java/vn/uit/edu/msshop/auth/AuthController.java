package vn.uit.edu.msshop.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.auth.domain.dto.request.CreateAccountRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
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

    @PostMapping("/sign_up")
    public ResponseEntity<Void> createAccount(@RequestBody CreateAccountRequest request) {
        authService.createUser(request);
        return ResponseEntity.noContent().build();

    }
}
