package api.giybat.uz.service;

import api.giybat.uz.entity.EmailHistoryEntity;
import api.giybat.uz.enums.EmailType;
import api.giybat.uz.enums.Language;
import api.giybat.uz.exceptions.AppBadException;
import api.giybat.uz.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmailHistoryService {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;

    public void create(String email, String message, String code, EmailType emailType) {
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setEmail(email);
        entity.setMessage(message);
        entity.setCode(code);
        entity.setEmailType(emailType   );
        emailHistoryRepository.save(entity);
    }
    public Long getEmailCount(String email) {
        LocalDateTime now = LocalDateTime.now();
        return emailHistoryRepository.countAllByEmailAndCreatedDateBetween(email,now.minusMinutes(3),now);
    }
    public void check(String email, String code, Language language) {
        Optional<EmailHistoryEntity>optional=emailHistoryRepository.findTopByEmailOrderByCreatedDateDesc(email);
        if (optional.isEmpty())
            throw new AppBadException(resourceBundleService.getMessage("user.not.found",language));
        EmailHistoryEntity entity=optional.get();
        if(entity.getAttemptCount()>=3){
            throw new AppBadException(resourceBundleService.getMessage("attempt.limit.reached",language));
        }
        if(!entity.getCode().equals(code)){
            emailHistoryRepository.updateAttemptCount(entity.getId());
            throw new AppBadException(resourceBundleService.getMessage("wrong.code", language));

        }
        LocalDateTime expDate = entity.getCreatedDate().plusMinutes(2);
        if(LocalDateTime.now().isAfter(expDate))
            throw new AppBadException(resourceBundleService.getMessage("sms.code.expired",language));

    }
}
