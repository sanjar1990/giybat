package api.giybat.uz.repository;

import api.giybat.uz.dto.FilterResultDTO;
import api.giybat.uz.dto.profile.ProfileFilterDTO;
import api.giybat.uz.mapper.ProfileMapperDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.*;

@Repository
public class CustomProfileRepository {
    @Autowired
    private EntityManager entityManager;
    public FilterResultDTO<ProfileMapperDTO> filterProfile(ProfileFilterDTO dto, int page, int size){
        StringBuilder selectBuilder = new StringBuilder("select p.id as id, p.name as name, p.username as username ," +
                " p.status as status,  p.attach_id as attachId , p.created_date as createdDate ," +
                " (select string_agg(r.role,',')) as roles," +
                "                (select count(*) from post  where post.profile_id=p.id) as postCount " +
                "               from profile as p inner join  profile_role as r on p.id=r.profile_id  ");
        StringBuilder countBuilder = new StringBuilder("select count(p) from profile as p inner join  profile_role as r on p.id=r.profile_id");
        StringBuilder whereBuilder = new StringBuilder(" where 1=1");
        Map<String,Object> params = new HashMap<>();
        if(dto.getName()!=null && !dto.getName().isEmpty()){
            whereBuilder.append(" and lower(p.name) like :name ");
            params.put("name","%"+dto.getName().toLowerCase()+"%");
        }
        if(dto.getVisible()!=null){
            whereBuilder.append(" and p.visible = :visible ");
            params.put("visible",dto.getVisible());
        }
        if(dto.getFrom()!=null){
            whereBuilder.append(" and p.created_date >= :from ");
            params.put("from",dto.getFrom().atStartOfDay());
        }
        if(dto.getTo()!=null){
            whereBuilder.append(" and p.createdDate <= :to ");
            params.put("to", LocalDateTime.of(dto.getFrom(), LocalTime.MAX));
        }
        if(dto.getStatus()!=null){
            whereBuilder.append(" and p.status = :status ");
            params.put("status",dto.getStatus().name());
        }
        if(dto.getRole()!=null){
            whereBuilder.append(" and r.role = :role ");
            params.put("role",dto.getRole().name());
        }
        selectBuilder.append(whereBuilder);
        countBuilder.append(whereBuilder);
        selectBuilder.append(" group by p.id order by p.created_date desc");
        Query selectQuery = entityManager.createNativeQuery(selectBuilder.toString(),
                ProfileMapperDTO.class);
        selectQuery.setFirstResult(page * size);
        selectQuery.setMaxResults(size);
        Query countQuery = entityManager.createNativeQuery(countBuilder.toString(),Long.class);
        for (Map.Entry<String,Object> entry : params.entrySet()) {
            selectQuery.setParameter(entry.getKey(),entry.getValue());
            countQuery.setParameter(entry.getKey(),entry.getValue());
        }
        List<ProfileMapperDTO> list=selectQuery.getResultList();
//        System.out.println("TITLE::"+list.getFirst().getName());
        System.out.println("SIZE:"+list.size());
        Long totalCount = (Long) countQuery.getSingleResult();
        System.out.println("COUNT:"+totalCount);
        return new FilterResultDTO<>(totalCount,list);
    }
}
