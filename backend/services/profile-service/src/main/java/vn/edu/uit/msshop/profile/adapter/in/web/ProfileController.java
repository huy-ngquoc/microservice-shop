package vn.edu.uit.msshop.profile.adapter.in.web;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.edu.uit.msshop.profile.adapter.in.web.mapper.ProfileWebMapper;
import vn.edu.uit.msshop.profile.adapter.in.web.request.CreateProfileRequest;
import vn.edu.uit.msshop.profile.adapter.in.web.request.UpdateProfileRequest;
import vn.edu.uit.msshop.profile.adapter.in.web.response.AvatarResponse;
import vn.edu.uit.msshop.profile.adapter.in.web.response.ProfileResponse;
import vn.edu.uit.msshop.profile.application.dto.command.ReplaceAvatarCommand;
import vn.edu.uit.msshop.profile.application.port.in.CreateProfileUseCase;
import vn.edu.uit.msshop.profile.application.port.in.FindAvatarUseCase;
import vn.edu.uit.msshop.profile.application.port.in.FindProfileUseCase;
import vn.edu.uit.msshop.profile.application.port.in.ReplaceAvatarUseCase;
import vn.edu.uit.msshop.profile.application.port.in.UpdateProfileUseCase;
import vn.edu.uit.msshop.profile.domain.model.valueobject.ProfileId;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final FindProfileUseCase findUseCase;
    private final CreateProfileUseCase createUseCase;
    private final UpdateProfileUseCase updateUseCase;
    private final FindAvatarUseCase findAvatarUseCase;
    private final ReplaceAvatarUseCase replaceAvatarUseCase;
    private final ProfileWebMapper webMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> findById(
            @PathVariable
            final UUID id) {
        final var view = this.findUseCase.findById(new ProfileId(id));
        final var response = this.webMapper.toResponse(view);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<AvatarResponse> findAvatarById(
            @PathVariable
            final UUID id) {
        final var view = this.findAvatarUseCase.findById(new ProfileId(id));
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
    public ResponseEntity<Void> update(
            final UpdateProfileRequest request) {
        final var command = this.webMapper.toCommand(request);
        this.updateUseCase.update(command);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(
            value = "/{id}/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AvatarResponse> replaceAvatar(
            @PathVariable
            final UUID id,
            @RequestPart("file")
            final MultipartFile file)
            throws IOException {
        if (!file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        final String contentType = file.getContentType();
        if ((contentType == null) || (!contentType.startsWith("image/"))) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }

        final var cmd = new ReplaceAvatarCommand(
                new ProfileId(id),
                file.getBytes(),
                (file.getOriginalFilename() == null) ? "avatar" : file.getOriginalFilename(),
                contentType);

        final var view = this.replaceAvatarUseCase.replace(cmd);
        final var response = this.webMapper.toResponse(view);

        return ResponseEntity.ok(response);
    }
}
