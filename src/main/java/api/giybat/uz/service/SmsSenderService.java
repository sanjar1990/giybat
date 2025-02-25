package api.giybat.uz.service;

import api.giybat.uz.dto.sms.SmsAutRequestDTO;
import api.giybat.uz.dto.sms.SmsAuthResponseDTO;
import api.giybat.uz.dto.sms.SmsSendRequestDTO;
import api.giybat.uz.dto.sms.SmsSendResponseDTO;
import api.giybat.uz.entity.SmsProviderEntity;
import api.giybat.uz.enums.Language;
import api.giybat.uz.enums.SmsType;
import api.giybat.uz.exceptions.AppBadException;
import api.giybat.uz.repository.SmsProviderRepository;
import api.giybat.uz.util.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class SmsSenderService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SmsProviderRepository providerRepository;
    @Autowired
    private SmsHistoryService historyService;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Value("${eskiz.url}")
    private String eskizUrl;
    @Value("${eskiz.login}")
    private String eskizEmail;
    @Value("${eskiz.password}")
    private String eskizPassword;
    private final Integer smsLimit = 3;

    public void sendRegistrationSms(String phone, Language language) {
        String code = RandomUtil.getRandomSmsCode();
        String message = resourceBundleService.getMessage("eskiz.sms", language);
        sendSms(phone, message, code, SmsType.REGISTRATION, language);
        System.out.println("CODE::" + code);
    }

    public void sendResetPasswordSms(String phone, Language lang) {
        String code = RandomUtil.getRandomSmsCode();
        String message = resourceBundleService.getMessage("eskiz.sms", lang);
        sendSms(phone, message, code, SmsType.RESET_PASSWORD, lang);
        System.out.println("CODE::" + code);
    }
    public void updateUsernamePasswordSms(String phone, Language lang) {
        String code = RandomUtil.getRandomSmsCode();
        String message = resourceBundleService.getMessage("eskiz.sms", lang);
        sendSms(phone, message, code, SmsType.CHANGE_USERNAME, lang);
        System.out.println("CODE::" + code);
    }

    private SmsSendResponseDTO sendSms(String phone, String message, String code, SmsType smsType, Language language) {
        Long count = historyService.getSmsCount(phone);
        if (count >= smsLimit) {
            log.warn("SmsLimit reached");
            throw new AppBadException(resourceBundleService.getMessage("sms.count.reached", language));
        }
        SmsSendResponseDTO response = sendSms(phone, message);
        historyService.create(phone, message, code, smsType);
        return response;
    }

    private SmsSendResponseDTO sendSms(String phoneNumber, String message) {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");
        SmsSendRequestDTO dto = new SmsSendRequestDTO();
        dto.setMessage(message);
        dto.setMobile_phone(phoneNumber);
        HttpEntity<SmsSendRequestDTO> entity = new HttpEntity<>(dto, headers);
        String url = eskizUrl + "/message/sms/send";
        try {
            ResponseEntity<SmsSendResponseDTO> response = restTemplate.postForEntity(url, entity, SmsSendResponseDTO.class);
            System.out.println(response.getBody());
            System.out.println("Sms send::" + response.getBody());
            return response.getBody();

        } catch (RuntimeException e) {
            log.warn(e.toString());

            e.printStackTrace();
        }

        return null;
    }

    private String getToken() {
        Optional<SmsProviderEntity> optional = providerRepository.findTopBy();
        if (optional.isEmpty()) {
            SmsProviderEntity entity = new SmsProviderEntity();
            entity.setToken(getTokenFromProvider());
            providerRepository.save(entity);
            return getTokenFromProvider();
        } else {
            SmsProviderEntity entity = optional.get();
            if (LocalDateTime.now().isBefore(entity.getExpiredDate())) {
                return entity.getToken();
            } else {
                String token = getTokenFromProvider();
                entity.setToken(token);
                entity.setCreatedDate(LocalDateTime.now());
                entity.setExpiredDate(LocalDateTime.now().plusDays(30));
                providerRepository.save(entity);
                return token;
            }
        }
    }

    private String getTokenFromProvider() {
        SmsAutRequestDTO autDTO = new SmsAutRequestDTO();
        autDTO.setEmail(eskizEmail);
        autDTO.setPassword(eskizPassword);
        SmsAuthResponseDTO responseDTO;
        try {
            ResponseEntity<SmsAuthResponseDTO> response = restTemplate.postForEntity(eskizUrl + "/auth/login", autDTO, SmsAuthResponseDTO.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                responseDTO = response.getBody();
                System.out.println(responseDTO);
                if (responseDTO != null) {
                    return responseDTO.getData().getToken();
                }
            } else {
                return null;
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
        }

//        try {
//            String response = restTemplate.postForObject(eskizUrl+"/auth/login", autDTO, String.class);
//            JsonNode parent=new ObjectMapper().readTree(response);
//            JsonNode data=parent.get("data");
//            String token=data.get("token").asText();
//
//            return token;
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;
    }

}
