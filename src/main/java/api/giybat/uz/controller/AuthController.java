package api.giybat.uz.controller;

import api.giybat.uz.dto.ApiResponseDTO;
import api.giybat.uz.dto.JwtDTO;
import api.giybat.uz.dto.auth.ResetPasswordConfirmDTO;
import api.giybat.uz.dto.auth.ResetPasswordDTO;
import api.giybat.uz.dto.profile.ProfileDTO;
import api.giybat.uz.dto.auth.LoginDTO;
import api.giybat.uz.dto.auth.RegistrationDTO;
import api.giybat.uz.dto.sms.SmsResendDTO;
import api.giybat.uz.dto.sms.SmsVerificationDTO;
import api.giybat.uz.enums.Language;
import api.giybat.uz.service.AuthService;
import api.giybat.uz.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


//@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth api list",description = "This api is for auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/emailReg")
    @Operation(summary = "Registration",description = "this api used for registration")
    public ResponseEntity<ApiResponseDTO> registration(@Valid @RequestBody RegistrationDTO dto,
                                                       @RequestHeader(value = "Accept-Language",defaultValue = "uz")Language lang) {
//        log.info("login authDTO: {}", dto);
        return ResponseEntity.ok(authService.registration(dto,lang));
    }
    @Operation(summary = " email verification ",description = "this api is for email verification")
    @GetMapping("/verification/email/{jwt}")
    public ResponseEntity<String>emailVerification(@PathVariable("jwt") String jwt,
                                                   @RequestParam(value = "lang",defaultValue = "uz") Language lang) {
        return ResponseEntity.ok(authService.emailRegistrationVerification(jwt,lang));
    }
    @Operation(summary = " sms verification ",description = "this api is for sms verification")
    @PostMapping("/verification/sms")
    public ResponseEntity<ProfileDTO>smsVerification( @Valid @RequestBody SmsVerificationDTO dto,
                                                   @RequestHeader(value = "Accept-Language",defaultValue = "uz")Language lang) {
        return ResponseEntity.ok(authService.smsRegistrationVerification(dto,lang));
    }
    @Operation(summary = " sms resend ",description = "this api is for sms resend")
    @PostMapping("/verification/sms/resend")
    public ResponseEntity<String>smsVerificationResend( @Valid @RequestBody SmsResendDTO dto,
                                                      @RequestHeader(value = "Accept-Language",defaultValue = "uz")Language lang) {
        return ResponseEntity.ok(authService.smsVerificationResend(dto,lang));
    }
    @PostMapping("/login")
    @Operation(summary = " Login ",description = "this api is for Login")
    public ResponseEntity<ProfileDTO> login(@Valid @RequestBody LoginDTO dto,
                                                 @RequestHeader(value = "Accept-Language",defaultValue = "uz")Language lang){
        return ResponseEntity.ok(authService.login(dto,lang));
    }
    @GetMapping("/test")
    public void test(){
        JwtDTO dto= JwtUtil.decodeJwt("eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJiZGI5YjM0My1hMmMyLTQ2NWYtOTdiNy02ZjI2NTY5M2RjZjEiLCJpYXQiOjE3NDAxMzg1NDEsImV4cCI6MTc0MDE0NTc0MSwiaXNzIjoiR2l5YmF0IHRlc3QgcG9ydGFsaSIsImlkIjoiYmRiOWIzNDMtYTJjMi00NjVmLTk3YjctNmYyNjU2OTNkY2YxIiwicm9sZSI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIl19.WYsL-nyqDdkq2x6ry2CDmKAmihVP3AKsyDjx_J-TwjODWp21wFoHIKZqW9lmznCK");
        System.out.println(dto);
    }
    @PostMapping("/resetPassword")
    @Operation(summary = " reset password ",description = "this api is for reset password")
    public ResponseEntity<ApiResponseDTO>resetPassword(@Valid @RequestBody ResetPasswordDTO dto,
                                               @RequestHeader(value = "Accept-Language",defaultValue = "uz") Language lang) {
        return ResponseEntity.ok(authService.resetPassword(dto,lang));

    }
    @PostMapping("/resetPassword/confirm")
    @Operation(summary = " reset password confirm ",description = "this api is for reset password confirm")
    public ResponseEntity<ApiResponseDTO>resetPasswordConfirm(@Valid @RequestBody ResetPasswordConfirmDTO dto,
                                                       @RequestHeader(value = "Accept-Language",defaultValue = "uz") Language lang) {
        return ResponseEntity.ok(authService.resetPasswordConfirm(dto,lang));
    }



}
