package api.giybat.uz.service;

import api.giybat.uz.config.SpringSecurityUtil;
import api.giybat.uz.dto.FilterResultDTO;
import api.giybat.uz.dto.post.*;
import api.giybat.uz.dto.profile.ProfileDTO;
import api.giybat.uz.entity.PostEntity;
import api.giybat.uz.enums.Language;
import api.giybat.uz.enums.ProfileRole;
import api.giybat.uz.exceptions.Forbidden;
import api.giybat.uz.exceptions.ItemNotFoundException;
import api.giybat.uz.repository.CustomPostRepository;
import api.giybat.uz.repository.PostRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private CustomPostRepository customPostRepository;


    public PostDTO createPost(PostCreateDTO dto, Language language) {
        PostEntity entity = new PostEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setAttachId(dto.getAttachId());
        entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        entity.setVisible(Boolean.TRUE);
        postRepository.save(entity);
return toInfoDTO(entity);
    }
    public PageImpl<PostDTO> getProfilePostList(int page,int size,Language language) {
        String profileId = SpringSecurityUtil.getCurrentUserId();
        Pageable pageable=PageRequest.of(page,size,Sort.by(Sort.Direction.ASC, "createdDate"));
        Page<PostEntity> pageObj=postRepository.findAllByProfileIdAndVisibleTrueOrderByCreatedDateDesc(profileId,pageable);
        return new PageImpl<>(pageObj.getContent().stream().map(this::toInfoDTO).toList(), pageable, pageObj.getTotalElements());
    }
    public PostDTO getPostById(String postId) {
        return toDTO(getById(postId));
    }
    private PostEntity getById(String id) {
        return postRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("No post found with id: " + id));
    }
    private PostDTO toDTO(PostEntity entity) {
        PostDTO dto = new PostDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setCreatedAt(entity.getCreatedDate());
        dto.setAttach(attachService.getAttachDTO(entity.getAttachId()));
    return dto;
    }
    private PostDTO toInfoDTO(PostEntity entity) {
        PostDTO dto = new PostDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setCreatedAt(entity.getCreatedDate());
        dto.setAttach(attachService.getAttachDTO(entity.getAttachId()));
        return dto;
    }


    public PostDTO updatePost(String id, @Valid PostCreateDTO dto, Language language) {
        PostEntity entity = getById(id);
        if(!SpringSecurityUtil.hasRole(ProfileRole.ROLE_ADMIN)
                && !entity.getProfileId().equals(SpringSecurityUtil.getCurrentUserId())) {
            throw new Forbidden("You do not have permission to update this post");
        }
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        if(dto.getAttachId() != null && !entity.getAttachId().equals(dto.getAttachId())) {
            attachService.delete(entity.getAttachId());
            entity.setAttachId(dto.getAttachId());
        }

        entity.setVisible(Boolean.TRUE);
        postRepository.save(entity);
        return toInfoDTO(entity);
    }

    public Boolean deleteById(String id, Language language) {
        PostEntity entity = getById(id);
        if(!SpringSecurityUtil.hasRole(ProfileRole.ROLE_ADMIN)
                && !entity.getProfileId().equals(SpringSecurityUtil.getCurrentUserId())) {
            throw new Forbidden("You do not have permission to update this post");
        }
        return postRepository.deletePost(id)>0;
    }

    public PageImpl<PostDTO> filterSearchPost(Integer page,Integer size, PostFilterDTO dto, Language language) {
        FilterResultDTO<PostEntity> filterResult=customPostRepository.postFilter(dto,page,size);
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdDate").descending());
        List<PostDTO> dtoList=filterResult.getContent().stream().map(this::toDTO).collect(Collectors.toList());
        return new PageImpl<>(dtoList,pageable,filterResult.getTotalCount());
    }

    public List<PostDTO> getLastThree(String id, Language language) {
        return postRepository.getLastThreePosts(id).stream().map(this::toDTO).toList();
    }

    public PageImpl<PostDTO> filterPostsForAdmin(int page, Integer size, PostFilterAdminDTO dto, Language language) {
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdDate").descending());
        FilterResultDTO<PostAdminFilterMapperDTO> result= customPostRepository.postFilter(dto,page,size);
        List<PostDTO> dtoList=result.getContent().stream().map(s->{
            PostDTO post=new PostDTO();
            post.setId(s.getPostId());
            post.setTitle(s.getTitle());
            post.setAttach(attachService.getAttachDTO(s.getPostAttach()));
            post.setCreatedAt(s.getCreatedDate());
            ProfileDTO profile=new ProfileDTO();
            profile.setId(s.getProfileId());
            profile.setUsername(s.getUsername());
            profile.setName(s.getProfileName());
            profile.setAttach(attachService.getAttachDTO(s.getProfileAttach()));
            post.setProfile(profile);
            return post;
        }).toList();

        return new PageImpl<>(dtoList,pageable,result.getTotalCount());
    }
}
