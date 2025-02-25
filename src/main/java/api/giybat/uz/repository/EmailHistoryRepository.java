package api.giybat.uz.repository;

import api.giybat.uz.entity.EmailHistoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity,String> {
    Long countAllByEmailAndCreatedDateBetween(String email, LocalDateTime createdDateAfter,
                                              LocalDateTime createdDateBefore);
    Optional<EmailHistoryEntity>findTopByEmailOrderByCreatedDateDesc(String email);
    @Transactional
    @Modifying
    @Query("update EmailHistoryEntity set attemptCount=coalesce(attemptCount,0)+1 where id=?1")
    void updateAttemptCount(String id);
}
