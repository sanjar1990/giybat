package api.giybat.uz.repository;

import api.giybat.uz.entity.SmsHistoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SmsHistoryRepository extends CrudRepository<SmsHistoryEntity,String> {
    Long countAllByVisibleTrueAndPhoneAndCreatedDateBetween(String phone,
                                                            LocalDateTime createdDateAfter,
                                                            LocalDateTime createdDateBefore);

    Optional<SmsHistoryEntity> findTopByPhoneOrderByCreatedDateDesc(String phoneNumber);
    @Transactional
    @Modifying
    @Query("update SmsHistoryEntity set attemptCount =coalesce(attemptCount,0)+1 where id=?1")
    void incrementAttemptCount(String id);

}
