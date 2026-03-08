package vn.uit.edu.msshop.auth;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
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
