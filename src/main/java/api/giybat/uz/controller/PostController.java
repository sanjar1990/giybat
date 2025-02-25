package api.giybat.uz.controller;

import api.giybat.uz.config.SpringSecurityUtil;
import api.giybat.uz.entity.ProfileEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {
    @PostMapping()
    public String post() {
        ProfileEntity profile=SpringSecurityUtil.getCurrentUser();
        System.out.println(profile.getUsername());
        return "post";
    }
}
