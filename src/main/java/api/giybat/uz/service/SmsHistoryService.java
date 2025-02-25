package api.giybat.uz.service;

import api.giybat.uz.entity.SmsHistoryEntity;
import api.giybat.uz.enums.Language;
import api.giybat.uz.enums.SmsType;
import api.giybat.uz.exceptions.AppBadException;
import api.giybat.uz.repository.SmsHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SmsHistoryService {
    @Autowired
    private SmsHistoryRepository smsHistoryRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;

    public void create(String phoneNumber, String message, String code, SmsType smsType) {
        SmsHistoryEntity entity = new SmsHistoryEntity();
        entity.setPhone(phoneNumber);
        entity.setCode(code);
        entity.setMessage(message);
        entity.setSmsType(smsType);
        entity.setAttemptCount(0);
        smsHistoryRepository.save(entity);
    }
    public Long getSmsCount(String phoneNumber) {
        LocalDateTime now = LocalDateTime.now();
        return smsHistoryRepository.countAllByVisibleTrueAndPhoneAndCreatedDateBetween(phoneNumber, now.minusMinutes(3),now);
    }
    public void check(String phoneNumber, String code,Language language) {
        Optional<SmsHistoryEntity> optional =smsHistoryRepository.findTopByPhoneOrderByCreatedDateDesc(phoneNumber);
        if (optional.isEmpty()) throw new AppBadException(resourceBundleService.getMessage("wrong.code",language));
        SmsHistoryEntity entity = optional.get();
        if(entity.getAttemptCount()>=3)
            throw new AppBadException(resourceBundleService.getMessage("attempt.limit.reached",language));
        if(!entity.getCode().equals(code) ){
            smsHistoryRepository.incrementAttemptCount(entity.getId());
            throw new AppBadException(resourceBundleService.getMessage("wrong.code",language));
        }
        if (LocalDateTime.now().isAfter(entity.getCreatedDate().plusMinutes(2)))
            throw new AppBadException(resourceBundleService.getMessage("sms.code.expired",language));
    }

}
