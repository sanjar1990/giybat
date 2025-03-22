package api.giybat.uz.controller;

import api.giybat.uz.dto.post.PostCreateDTO;
import api.giybat.uz.dto.post.PostDTO;
import api.giybat.uz.dto.post.PostFilterAdminDTO;
import api.giybat.uz.dto.post.PostFilterDTO;
import api.giybat.uz.enums.Language;
import api.giybat.uz.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/post")
@Tag(name = "Post controller", description = "This is set of post controller api")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping()
    @Operation(summary = " post create ", description = "this api is for creating post")
    public ResponseEntity<PostDTO> createPost(@Valid @RequestBody PostCreateDTO dto,
                                              @RequestHeader(value = "Accept-Language", defaultValue = "uz") Language language) {
        return ResponseEntity.ok(postService.createPost(dto, language));
    }

    @PutMapping("/{id}")
    @Operation(summary = " post update ", description = "this api is for updating post")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable("id") String id,
            @Valid @RequestBody PostCreateDTO dto,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") Language language) {
        return ResponseEntity.ok(postService.updatePost(id,dto, language));
    }

    @GetMapping("/myPosts")
    @Operation(summary = "To get my posts ", description = "this api is for getting own posts")
    public ResponseEntity<PageImpl<PostDTO>> getProfilePostList(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "size",defaultValue = "10")Integer size,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") Language language) {

        return ResponseEntity.ok(postService.getProfilePostList(page-1,size,language));
    }

    @GetMapping("/public/{id}")
    @Operation(summary = "To get post by id ", description = "this api is for post by id")
    public ResponseEntity<PostDTO> getById(
            @PathVariable("id") String id,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") Language language) {
        return ResponseEntity.ok(postService.getPostById(id));
    }
    @GetMapping("/public/lastThree/{id}")
    @Operation(summary = "To get posts last three ", description = "this api is for get last three")
    public ResponseEntity<List<PostDTO>> getLastThree(
            @PathVariable("id") String id,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") Language language) {
        return ResponseEntity.ok(postService.getLastThree(id,language));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "To delete post by id ", description = "this api is for deleting post by id")
    public ResponseEntity<Boolean> deleteById(
            @PathVariable("id") String id,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") Language language) {
        return ResponseEntity.ok(postService.deleteById(id,language));
    }
    @PostMapping("/public/filterPost")
    @Operation(summary = "To filter search posts ", description = "this api is for search posts")
    public ResponseEntity<PageImpl<PostDTO>> filterSearchPost(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "size",defaultValue = "10")Integer size,
            @RequestBody PostFilterDTO dto,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") Language language) {

        return ResponseEntity.ok(postService.filterSearchPost(page-1,size,dto,language));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/filterAdmin")
    @Operation(summary = "To filter  posts for admin ", description = "this api is for filtering posts for admin")
    public ResponseEntity<PageImpl<PostDTO>> filterPostsForAdmin(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "size",defaultValue = "10")Integer size,
            @RequestBody PostFilterAdminDTO dto,
            @RequestHeader(value = "Accept-Language", defaultValue = "uz") Language language) {

        return ResponseEntity.ok(postService.filterPostsForAdmin(page-1,size,dto,language));
    }
}
