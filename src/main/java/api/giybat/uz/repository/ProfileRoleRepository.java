package api.giybat.uz.repository;

import api.giybat.uz.entity.ProfileRolesEntity;
import api.giybat.uz.enums.ProfileRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProfileRoleRepository extends CrudRepository<ProfileRolesEntity, Long> {
    @Transactional
    @Modifying
    void deleteByProfileId(String profileId);
    @Query("select role from ProfileRolesEntity where profileId=?1")
    List<ProfileRole> getRolesByProfileID(String profileId);
}
