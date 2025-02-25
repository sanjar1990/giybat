package api.giybat.uz.service;

import api.giybat.uz.config.SpringSecurityUtil;
import api.giybat.uz.dto.ApiResponseDTO;
import api.giybat.uz.dto.profile.CodeConfirmDTO;
import api.giybat.uz.dto.profile.ProfileDetailUpdateDTO;
import api.giybat.uz.dto.profile.ProfilePasswordUpdateDTO;
import api.giybat.uz.dto.profile.ProfileUsernameUpdateDTO;
import api.giybat.uz.entity.ProfileEntity;
import api.giybat.uz.enums.Language;
import api.giybat.uz.enums.ProfileRole;
import api.giybat.uz.exceptions.AppBadException;
import api.giybat.uz.exceptions.ItemNotFoundException;
import api.giybat.uz.repository.ProfileRepository;
import api.giybat.uz.util.EmailUtil;
import api.giybat.uz.util.JwtUtil;
import api.giybat.uz.util.PhoneUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProfileService {
@Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private SmsSenderService smsSenderService;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private SmsHistoryService smsHistoryService;
    @Autowired
    private EmailHistoryService emailHistoryService;
    @Autowired
    private ProfileRoleService profileRoleService;

    public ApiResponseDTO updateDetails(@Valid ProfileDetailUpdateDTO dto, Language language) {
        String  profileId= SpringSecurityUtil.getCurrentUserId();
//        ProfileEntity profile=getProfile(profileId, language);
        profileRepository.updateProfileDetail(dto.getName(),profileId);
        return new ApiResponseDTO(false,"Profile updated successfully");
    }


    public ApiResponseDTO updatePassword(@Valid ProfilePasswordUpdateDTO dto, Language language) {
        String  profileId= SpringSecurityUtil.getCurrentUserId();
        ProfileEntity entity=getProfile(profileId,language);
        if(!bCryptPasswordEncoder.matches(dto.getCurrentPassword(),entity.getPassword()))
            throw new AppBadException(resourceBundleService.getMessage("password.not.match",language));
        profileRepository.updatePassword(profileId,bCryptPasswordEncoder.encode(dto.getNewPassword()));
        return new ApiResponseDTO(false,"Password updated successfully");
    }

    private ProfileEntity getProfile(String profileId,Language language) {
        return profileRepository.findByIdAndVisibleTrue(profileId)
                .orElseThrow(()->new ItemNotFoundException(resourceBundleService.getMessage("user.not.found",language)));
    }

    public ApiResponseDTO updateUsername( ProfileUsernameUpdateDTO dto, Language language) {
     profileRepository.findByUsernameAndVisibleTrue(dto.getUsername())
              .ifPresent(s-> {throw new AppBadException(resourceBundleService.getMessage("username.exists",language));} );
//        if(optional.isPresent()) throw new AppBadException(resourceBundleService.getMessage("user.exists",language));
        if(PhoneUtil.isPhoneNumber(dto.getUsername())){
            smsSenderService.updateUsernamePasswordSms(dto.getUsername(),language);
        } else if (EmailUtil.isEmail(dto.getUsername())) {
            mailSenderService.sendChangeUsernameConfirmMail(dto.getUsername(),language);
        }
        String  profileId= SpringSecurityUtil.getCurrentUserId();
        profileRepository.setTempUserName(profileId,dto.getUsername());
        return new ApiResponseDTO(false,resourceBundleService.getMessage("sms.code.sent",language));
    }

    public ApiResponseDTO updateUsernameConfirm( CodeConfirmDTO dto, Language language) {
        String  profileId= SpringSecurityUtil.getCurrentUserId();
        ProfileEntity entity=getProfile(profileId,language);
        String tempUserName=entity.getTempUsername();
        if(PhoneUtil.isPhoneNumber(tempUserName)){
            smsHistoryService.check(tempUserName,dto.getCode(),language);
        } else if (EmailUtil.isEmail(tempUserName)) {
            emailHistoryService.check(tempUserName,dto.getCode(),language);
        }
        profileRepository.updateUserName(profileId,tempUserName);
        List<ProfileRole> roleList=profileRoleService.getRoles(profileId);
        Object jwt= JwtUtil.encode(tempUserName,entity.getId(),roleList);
        return new ApiResponseDTO(false,jwt);
    }
}
