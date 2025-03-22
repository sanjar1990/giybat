package api.giybat.uz.controller;

import api.giybat.uz.dto.ApiResponseDTO;
import api.giybat.uz.dto.auth.RegistrationDTO;
import api.giybat.uz.dto.profile.*;
import api.giybat.uz.entity.ProfileEntity;
import api.giybat.uz.enums.Language;
import api.giybat.uz.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "Profile api list",description = "This api is for profile")

public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PutMapping("/detail")
    @Operation(summary = "update detail",description = "this api used for update detail")
    public ResponseEntity<ApiResponseDTO>updateDetails(@Valid @RequestBody ProfileDetailUpdateDTO dto,
                                                       @RequestHeader(value = "Accept-Language",defaultValue = "uz")Language language){
        return ResponseEntity.ok(profileService.updateDetails(dto,language));
    }
    @PutMapping("/password")
    @Operation(summary = "update password",description = "this api used for update password")
    public ResponseEntity<ApiResponseDTO>updatePassword(@Valid @RequestBody ProfilePasswordUpdateDTO dto,
                                                       @RequestHeader(value = "Accept-Language",defaultValue = "uz")Language language){
        return ResponseEntity.ok(profileService.updatePassword(dto,language));
    }
    @PutMapping("/updateUsername")
    @Operation(summary = "update username",description = "this api used for update username")
    public ResponseEntity<ApiResponseDTO>updateUsername(@Valid @RequestBody ProfileUsernameUpdateDTO dto,
                                                        @RequestHeader(value = "Accept-Language",defaultValue = "uz")Language language){
        return ResponseEntity.ok(profileService.updateUsername(dto,language));
    }
    @PutMapping("/updateUsername/confirm")
    @Operation(summary = "update username confirm",description = "this api used for update username confirm")
    public ResponseEntity<ApiResponseDTO>updateUsernameConfirm(@Valid @RequestBody CodeConfirmDTO dto,
                                                        @RequestHeader(value = "Accept-Language",defaultValue = "uz")Language language){
        return ResponseEntity.ok(profileService.updateUsernameConfirm(dto,language));
    }
    @PutMapping("/updatePhoto")
    @Operation(summary = "update photo",description = "this api used for update photo")
    public ResponseEntity<ApiResponseDTO>updatePhoto(@Valid @RequestBody ProfileAttachUpdateDTO dto,
                                                     @RequestHeader(value = "Accept-Language",defaultValue = "uz")Language language){
        return ResponseEntity.ok(profileService.updatePhoto(dto,language));
    }
    @PostMapping("/filter")
    @Operation(summary = "Profile filter",description = "this api used for filtering profile")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageImpl<ProfileDTO>>filterProfile(@Valid @RequestBody ProfileFilterDTO dto,
                                                                @RequestParam(value = "page",defaultValue = "1")Integer page,
                                                                @RequestParam(value = "size",defaultValue = "10")Integer size,
                                                                @RequestHeader(value = "Accept-Language",defaultValue = "uz")Language language){
        return ResponseEntity.ok(profileService.filterProfile(page-1,size,dto,language));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/changeStatus/{id}")
    @Operation(summary = "update profile status",description = "this api used for updating profile status")
    public ResponseEntity<Boolean>updateStatus(@PathVariable("id")String id,
                                                     @RequestBody ProfileStatusDTO dto,
                                                     @RequestHeader(value = "Accept-Language",defaultValue = "uz")Language language){
        return ResponseEntity.ok(profileService.updateStatus(id, dto.getStatus(),language));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "delete profile ",description = "this api used for deleting profile")
    public ResponseEntity<Boolean>delete(@PathVariable("id")String id,
                                         @RequestHeader(value = "Accept-Language",defaultValue = "uz")Language language){
        return ResponseEntity.ok(profileService.delete(id,language));
    }


}
