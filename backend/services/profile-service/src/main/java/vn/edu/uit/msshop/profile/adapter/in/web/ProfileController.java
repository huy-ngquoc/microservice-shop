package vn.edu.uit.msshop.profile.adapter.in.web;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.profile.adapter.in.web.mapper.ProfileWebMapper;
import vn.edu.uit.msshop.profile.adapter.in.web.request.CreateProfileRequest;
import vn.edu.uit.msshop.profile.adapter.in.web.request.UpdateProfileInfoRequest;
import vn.edu.uit.msshop.profile.adapter.in.web.response.ProfileResponse;
import vn.edu.uit.msshop.profile.application.port.in.CreateProfileUseCase;
import vn.edu.uit.msshop.profile.application.port.in.FindProfileUseCase;
import vn.edu.uit.msshop.profile.application.port.in.UpdateProfileInfoUseCase;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final FindProfileUseCase findUseCase;
    private final CreateProfileUseCase createUseCase;
    private final UpdateProfileInfoUseCase updateInfoUseCase;
    private final ProfileWebMapper webMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.findUseCase.findById(new ProfileId(id));
        final var response = this.webMapper.toResponse(view);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> create(
            final CreateProfileRequest request) {
        final var command = this.webMapper.toCommand(request);
        this.createUseCase.create(command);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<Void> updateInfo(
            final UpdateProfileInfoRequest request) {
        final var command = this.webMapper.toCommand(request);
        this.updateInfoUseCase.update(command);

        return ResponseEntity.noContent().build();
    }
}
