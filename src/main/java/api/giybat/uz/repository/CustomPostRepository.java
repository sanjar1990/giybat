package api.giybat.uz.repository;

import api.giybat.uz.dto.FilterResultDTO;
import api.giybat.uz.dto.post.PostAdminFilterMapperDTO;
import api.giybat.uz.dto.post.PostFilterAdminDTO;
import api.giybat.uz.dto.post.PostFilterDTO;
import api.giybat.uz.entity.PostEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class CustomPostRepository {
    @Autowired
    private EntityManager entityManager;
    public FilterResultDTO<PostEntity> postFilter(PostFilterDTO dto, int page, int size){
        StringBuilder selectBuilder = new StringBuilder("select p from PostEntity p");
        StringBuilder countBuilder = new StringBuilder("select count(p) from PostEntity p");
        StringBuilder whereBuilder = new StringBuilder(" where p.visible=true ");
        Map<String,Object>params = new HashMap<String,Object>();
        if(dto.getQuery() != null && !dto.getQuery().isEmpty()){
            whereBuilder.append(" and lower(p.title) like :query ");
            params.put("query", "%"+dto.getQuery().toLowerCase()+"%");
        }
        selectBuilder.append(whereBuilder);
        selectBuilder.append(" order by p.createdDate desc");
        countBuilder.append(whereBuilder);
        Query selectQuery = entityManager.createQuery(selectBuilder.toString());
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);
        Query countQuery = entityManager.createQuery(countBuilder.toString());

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
            selectQuery.setParameter(entry.getKey(), entry.getValue());
        }
        List<PostEntity> content=selectQuery.getResultList();
        Long count = (Long) countQuery.getSingleResult();
        return new FilterResultDTO<PostEntity>(count,content);
    }
    public FilterResultDTO<PostAdminFilterMapperDTO> postFilter(PostFilterAdminDTO dto, int page, int size){
        StringBuilder selectBuilder = new StringBuilder("select p.id as postId, p.title , p.attachId as postAttach," +
                " p.createdDate," +
                " pr.id as profileId, pr.name as profileName, pr.username, pr.attachId as profileAttach  from PostEntity p " +
                " inner join p.profile as pr ");
        StringBuilder countBuilder = new StringBuilder("select count(p) from PostEntity p inner join p.profile as pr ");
        StringBuilder whereBuilder = new StringBuilder(" where p.visible=true ");
        Map<String,Object>params = new HashMap<String,Object>();
        if(dto.getProfileQuery() != null && !dto.getProfileQuery().isEmpty()){
            whereBuilder.append(" and (lower(pr.name) like :profile_query or lower(pr.username) like :profile_query) ");
            params.put("profile_query", "%"+dto.getProfileQuery().toLowerCase()+"%");
        }
        if(dto.getPostQuery() != null && !dto.getPostQuery().isEmpty()){
            whereBuilder.append(" and (p.id = :post_id or lower(p.title) like :post_query) ");
            params.put("post_query", "%"+dto.getPostQuery().toLowerCase()+"%");
            params.put("post_id", dto.getPostQuery());
        }
        selectBuilder.append(whereBuilder);
        selectBuilder.append(" order by p.createdDate desc");
        countBuilder.append(whereBuilder);
        Query selectQuery = entityManager.createQuery(selectBuilder.toString(),PostAdminFilterMapperDTO.class);
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);
        Query countQuery = entityManager.createQuery(countBuilder.toString());

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
            selectQuery.setParameter(entry.getKey(), entry.getValue());
        }
        List<PostAdminFilterMapperDTO> content=selectQuery.getResultList();
        Long count = (Long) countQuery.getSingleResult();
        return new FilterResultDTO<PostAdminFilterMapperDTO>(count,content);
    }
}
