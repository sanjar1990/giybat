package api.giybat.uz.service;

import api.giybat.uz.dto.ApiResponseDTO;
import api.giybat.uz.dto.AttachDTO;
import api.giybat.uz.dto.auth.ResetPasswordConfirmDTO;
import api.giybat.uz.dto.auth.ResetPasswordDTO;
import api.giybat.uz.dto.profile.ProfileDTO;
import api.giybat.uz.dto.auth.LoginDTO;
import api.giybat.uz.dto.auth.RegistrationDTO;
import api.giybat.uz.dto.sms.SmsResendDTO;
import api.giybat.uz.dto.sms.SmsVerificationDTO;
import api.giybat.uz.entity.ProfileEntity;
import api.giybat.uz.enums.GeneralStatus;
import api.giybat.uz.enums.Language;
import api.giybat.uz.enums.ProfileRole;
import api.giybat.uz.exceptions.AppBadException;
import api.giybat.uz.exceptions.ItemNotFoundException;
import api.giybat.uz.repository.ProfileRepository;
import api.giybat.uz.util.EmailUtil;
import api.giybat.uz.util.JwtUtil;
import api.giybat.uz.util.PhoneUtil;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
//@Transactional
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ProfileRoleService profileRoleService;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private SmsSenderService smsSenderService;
    @Autowired
    private SmsHistoryService smsHistoryService;
    @Autowired
    private EmailHistoryService emailHistoryService;
    @Autowired
    private AttachService attachService;

    public ApiResponseDTO registration(RegistrationDTO dto, Language language) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (optional.isPresent()) {
            ProfileEntity entity = optional.get();
            if (entity.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
                profileRoleService.deleteRole(entity.getId());
                profileRepository.delete(entity);
            } else {
                throw new AppBadException(resourceBundleService.getMessage("username.already.exists", language));
            }
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setUsername(dto.getUsername());
        entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        entity.setStatus(GeneralStatus.IN_REGISTRATION);
        profileRepository.save(entity);
        profileRoleService.createRole(ProfileRole.ROLE_USER, entity.getId());
        if (PhoneUtil.isPhoneNumber(dto.getUsername())) {
            smsSenderService.sendRegistrationSms(dto.getUsername(), language);
            return new ApiResponseDTO(false, resourceBundleService.getMessage("sms.code.sent", language));
        } else if(EmailUtil.isEmail(dto.getUsername())) {
            mailSenderService.sendEmailVerification(entity.getUsername(), entity.getId(), entity.getName(), language);
            return new ApiResponseDTO(false, resourceBundleService.getMessage("verification.email", language));
        }else  return new ApiResponseDTO(false, resourceBundleService.getMessage("invalid.username", language));
        }


    public String emailRegistrationVerification(String jwt, Language language) {
        try {
            String profileId = JwtUtil.decode(jwt);
            ProfileEntity entity = profileRepository
                    .findByIdAndVisibleTrue(profileId).orElseThrow(() -> new ItemNotFoundException("Profile not found"));
            if (!entity.getStatus().equals(GeneralStatus.IN_REGISTRATION))
                throw new AppBadException("invalid verification code");
            entity.setStatus(GeneralStatus.ACTIVE);
            profileRepository.save(entity);

        } catch (JwtException e) {
            e.printStackTrace();
            throw new AppBadException(e.getMessage());
        }

        return "Account verified successfully";
    }

    public ProfileDTO login(LoginDTO dto, Language language) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (optional.isEmpty())
            throw new ItemNotFoundException(resourceBundleService.getMessage("user.not.found", language));
        ProfileEntity entity = optional.get();
        if (!bCryptPasswordEncoder.matches(dto.getPassword(), entity.getPassword()))
            throw new ItemNotFoundException("Username or password is incorrect");
        if (!entity.getStatus().equals(GeneralStatus.ACTIVE)) throw new AppBadException("Profile is blocked");

        return getLoginResponse(entity);
    }

    public ProfileDTO smsRegistrationVerification(SmsVerificationDTO dto, Language lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getPhone());
        if (optional.isEmpty())
            throw new ItemNotFoundException(resourceBundleService.getMessage("user.not.found", lang));
        ProfileEntity entity = optional.get();
        if (!entity.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
            throw new AppBadException(resourceBundleService.getMessage("verification.failed", lang));
        }
        smsHistoryService.check(dto.getPhone(), dto.getCode(), lang);
        entity.setStatus(GeneralStatus.ACTIVE);
        profileRepository.save(entity);
        return getLoginResponse(entity);

    }

    private ProfileDTO getLoginResponse(ProfileEntity entity) {
        List<ProfileRole> roleList = profileRoleService.getRoles(entity.getId());
        ProfileDTO profile = new ProfileDTO();
        profile.setName(entity.getName());
        profile.setUsername(entity.getUsername());
        profile.setRoleList(roleList);
        profile.setJwt(JwtUtil.encode(entity.getUsername(), entity.getId(), roleList));
        profile.setAttach(attachService.getAttachDTO(entity.getAttachId()));
        return profile;
    }

    public String smsVerificationResend(@Valid SmsResendDTO dto, Language lang) {
            Optional<ProfileEntity>optional=profileRepository.findByUsernameAndVisibleTrue(dto.getPhone());
            if (optional.isEmpty()) throw new ItemNotFoundException(resourceBundleService.getMessage("user.not.found", lang));
            ProfileEntity entity = optional.get();
            if (!entity.getStatus().equals(GeneralStatus.IN_REGISTRATION)) throw new AppBadException("wrong status");
            smsSenderService.sendRegistrationSms(dto.getPhone(), lang);
            return "Sms code sent";
    }

    public ApiResponseDTO resetPassword(ResetPasswordDTO dto, Language lang) {
        Optional<ProfileEntity> optional=profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if(optional.isEmpty()) throw new ItemNotFoundException(resourceBundleService.getMessage("user.not.found", lang));
        ProfileEntity entity=optional.get();
        if(!entity.getStatus().equals(GeneralStatus.ACTIVE))
            throw new AppBadException(resourceBundleService.getMessage("wrong.status", lang));
        if(PhoneUtil.isPhoneNumber(entity.getUsername())) {
            smsSenderService.sendResetPasswordSms(dto.getUsername(),lang);
        }
        if(EmailUtil.isEmail(dto.getUsername())) {
            mailSenderService.sendResetPasswordEmail(dto.getUsername(),lang);
        }
        String message=resourceBundleService.getMessage("send.verification.code", lang);
        message=String.format(message, entity.getUsername());
        return new ApiResponseDTO(false, message);
    }

    public ApiResponseDTO resetPasswordConfirm(ResetPasswordConfirmDTO dto, Language lang) {
        ProfileEntity profile=profileRepository.findByUsernameAndVisibleTrue(dto.getUsername())
                .orElseThrow(() -> new ItemNotFoundException(resourceBundleService.getMessage("user.not.found", lang)));
        if(!profile.getStatus().equals(GeneralStatus.ACTIVE))
            throw new AppBadException(resourceBundleService.getMessage("wrong.status", lang));
        if(PhoneUtil.isPhoneNumber(profile.getUsername())) {
            smsHistoryService.check(dto.getUsername(), dto.getCode(), lang);
        }else if(EmailUtil.isEmail(profile.getUsername())) {
            emailHistoryService.check(dto.getUsername(), dto.getCode(), lang);
        }
        profileRepository.updatePassword(profile.getId(),bCryptPasswordEncoder.encode(dto.getPassword()));
        return new ApiResponseDTO(false,"Password changed successfully");
    }
}
