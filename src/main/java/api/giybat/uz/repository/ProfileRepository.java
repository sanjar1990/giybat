package api.giybat.uz.repository;

import api.giybat.uz.entity.ProfileEntity;
import api.giybat.uz.enums.GeneralStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

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
    @Transactional
    @Modifying
    @Query("update ProfileEntity set attachId=?2 where id=?1")
    void updatePhoto(String profileId, String attachId);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set status=?2 where id=?1")
    int updateStatus(String id, GeneralStatus status);
    @Transactional
    @Modifying
    @Query("update ProfileEntity set visible=false where id=?1")
    int deleteProfile(String id);

//    @Query(value = "select p.id as id, p.name as name, p.username as username, " +
//            "                 p.status as status,  p.attach_id as attachId , p.created_date as createdDate, " +
//            "                 (select string_agg(r.role,',')) as roles," +
//            "                                (select count(*) from post  where post.profile_id=p.id) as postCount " +
//            "                               from profile as p inner join  profile_role as r on p.id=r.profile_id" +
//            " group by p.id order by p.created_date desc", nativeQuery = true)
//    List<ProfileMapperI> findFull();
}
