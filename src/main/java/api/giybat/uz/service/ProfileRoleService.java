package api.giybat.uz.service;

import api.giybat.uz.entity.ProfileRolesEntity;
import api.giybat.uz.enums.ProfileRole;
import api.giybat.uz.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProfileRoleService {
    @Autowired
    private ProfileRoleRepository profileRoleRepository;

    public void createRole(ProfileRole role,String id) {
        ProfileRolesEntity entity=new ProfileRolesEntity();
        entity.setRole(role);
        entity.setProfileId(id);
        profileRoleRepository.save(entity);
    }
    public void deleteRole(String profileId) {
        profileRoleRepository.deleteByProfileId(profileId);
    }
    public List<ProfileRole> getRoles(String profileId) {
        return profileRoleRepository.getRolesByProfileID(profileId);
    }
}
