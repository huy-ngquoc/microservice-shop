package vn.uit.edu.msshop.account.adapter.in.web;

import java.util.UUID;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.adapter.in.web.mapper.AccountWebMapper;
import vn.uit.edu.msshop.account.adapter.in.web.request.CreateAccountRequest;
import vn.uit.edu.msshop.account.adapter.in.web.request.LoginRequest;
import vn.uit.edu.msshop.account.adapter.in.web.request.UpdateAccountRequest;
import vn.uit.edu.msshop.account.adapter.in.web.request.UpdateAvatarRequest;
import vn.uit.edu.msshop.account.adapter.in.web.response.AccountResponse;
import vn.uit.edu.msshop.account.adapter.out.persistence.AccountOutboxEntityRepository;
import vn.uit.edu.msshop.account.adapter.out.persistence.SpringDataAccountJpaRepository;
import vn.uit.edu.msshop.account.application.port.in.CreateAccountUseCase;
import vn.uit.edu.msshop.account.application.port.in.FindAccountUseCase;
import vn.uit.edu.msshop.account.application.port.in.LoginUseCase;
import vn.uit.edu.msshop.account.application.port.in.LogoutUseCase;
import vn.uit.edu.msshop.account.application.port.in.RefreshTokenUseCase;
import vn.uit.edu.msshop.account.application.port.in.UpdateAccountUseCase;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountName;


@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final FindAccountUseCase findUseCase;
    private final CreateAccountUseCase createUseCase;
    private final UpdateAccountUseCase updateUseCase;
    private final AccountWebMapper webMapper;
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;
    private final AccountOutboxEntityRepository accountOutboxRepo;
    private final SpringDataAccountJpaRepository springDataAccountRepo;
    
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> findById(@PathVariable UUID id) {
        final var view = this.findUseCase.findAccountById(new AccountId(id));
        final var response = this.webMapper.toResponse(view);
        return ResponseEntity.ok(response);  
    }
    @PostMapping("/create") 
    public ResponseEntity<?> create(@RequestBody CreateAccountRequest request) {
        try {
        final var createAccountCommand = webMapper.toCommand(request);
        final var view= this.createUseCase.create(createAccountCommand);
        return ResponseEntity.ok(webMapper.toResponse(view));
        }
        catch(RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    } 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(loginUseCase.login(request));
        }
        catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body(e.getMessage());
        }
    }
    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
        try {
            return ResponseEntity.ok(refreshTokenUseCase.refreshToken(refreshToken));
        }
        catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body(e.getMessage());
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String refreshToken) {
        try {
            logoutUseCase.logout(refreshToken);
            return ResponseEntity.noContent().build();
        }
        catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body(e.getMessage());
        }
    }
    @PutMapping("/update")
    public ResponseEntity<Void> update(@RequestBody UpdateAccountRequest request) {
        final var updateAccountCommand = webMapper.toCommand(request);
        this.updateUseCase.update(updateAccountCommand);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/find_by_name")
    public ResponseEntity<?> getAccountByName(@RequestParam String name) {
        final var result = this.findUseCase.findByAccountName(new AccountName(name));
        if(result==null) return ResponseEntity.badRequest().body("Account not found");
        return ResponseEntity.ok(this.webMapper.toResponse(result));
    }

    @PutMapping("/update_avatar") 
    public ResponseEntity<Void> updateAvatar(@RequestBody UpdateAvatarRequest request) {
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearDB() {
        accountOutboxRepo.deleteAll();
        springDataAccountRepo.deleteAll();
        return ResponseEntity.noContent().build();
    }

}
