package api.giybat.uz.config;

import api.giybat.uz.entity.ProfileEntity;
import api.giybat.uz.enums.ProfileRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class SpringSecurityUtil {
    public static ProfileEntity getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user.getProfile();
    }

    public static String getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user.getProfile().getId();
    }
    public static Boolean hasRole(ProfileRole role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        authentication.getAuthorities().forEach(s-> System.out.println("ROLE:: "+s));
    return authentication.getAuthorities().stream().anyMatch(s->s.getAuthority().equals(role.name()));
    }
}
