package api.giybat.uz.repository;

import api.giybat.uz.entity.ProfileEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity, String> {
    Optional<ProfileEntity> findByUsernameAndVisibleTrue(String username);
    @Transactional
    @Modifying
    @Query("delete from ProfileEntity where id=?1")
    void deleteById(String id);

    Optional<ProfileEntity> findByIdAndVisibleTrue(String profileId);

    Optional<ProfileEntity> findByUsername(String username);
    @Transactional
    @Modifying
    @Query("update ProfileEntity set password=?2 where id=?1")
    void updatePassword(String id, String password);
    @Transactional
    @Modifying
    @Query("update ProfileEntity set name=?1 where id=?2")
    void updateProfileDetail(@NotBlank(message = "name is required") String name, String profileId);
    @Transactional
    @Modifying
    @Query("update ProfileEntity set tempUsername=?2 where id=?1")
    void setTempUserName(String profileId, String username);
    @Transactional
    @Modifying
    @Query("update ProfileEntity set username=?2 where id=?1")
    void updateUserName(String profileId, String tempUserName);
}
