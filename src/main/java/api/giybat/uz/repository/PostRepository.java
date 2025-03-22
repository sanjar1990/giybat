package api.giybat.uz.repository;

import api.giybat.uz.entity.PostEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<PostEntity,String>,
        PagingAndSortingRepository<PostEntity, String> {

    @Transactional
    @Modifying
    @Query("update PostEntity set visible=false where id=?1")
    int deletePost(String id);
    Page<PostEntity>findAllByProfileIdAndVisibleTrueOrderByCreatedDateDesc(String profileId, Pageable pageable);
    @Query("select p from PostEntity  as p where p.id !=?1 and p.visible=true order by p.createdDate desc limit 3")
    List<PostEntity>getLastThreePosts(String postId);
}
