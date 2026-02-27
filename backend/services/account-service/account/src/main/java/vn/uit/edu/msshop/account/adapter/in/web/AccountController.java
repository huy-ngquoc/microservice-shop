package vn.uit.edu.msshop.account.adapter.in.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.uit.edu.msshop.account.adapter.in.web.mapper.AccountWebMapper;
import vn.uit.edu.msshop.account.adapter.in.web.request.CreateAccountRequest;
import vn.uit.edu.msshop.account.adapter.in.web.request.UpdateAccountRequest;
import vn.uit.edu.msshop.account.adapter.in.web.response.AccountResponse;
import vn.uit.edu.msshop.account.application.port.in.CreateAccountUseCase;
import vn.uit.edu.msshop.account.application.port.in.FindAccountUseCase;
import vn.uit.edu.msshop.account.application.port.in.UpdateAccountUseCase;
import vn.uit.edu.msshop.account.domain.model.valueobject.AccountId;


@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final FindAccountUseCase findUseCase;
    private final CreateAccountUseCase createUseCase;
    private final UpdateAccountUseCase updateUseCase;
    private final AccountWebMapper webMapper;

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> findById(@PathVariable UUID id) {
        final var view = this.findUseCase.findAccountById(new AccountId(id));
        final var response = this.webMapper.toResponse(view);
        return ResponseEntity.ok(response);  
    }
    @PostMapping("/create") 
    public ResponseEntity<Void> create(CreateAccountRequest request) {
        final var createAccountCommand = webMapper.toCommand(request);
        this.createUseCase.create(createAccountCommand);
        return ResponseEntity.noContent().build();
    } 
    @PutMapping("/update")
    public ResponseEntity<Void> update(UpdateAccountRequest request) {
        final var updateAccountCommand = webMapper.toCommand(request);
        this.updateUseCase.update(updateAccountCommand);
        return ResponseEntity.noContent().build();
    }

}
