package api.giybat.uz.repository;

import api.giybat.uz.entity.SmsProviderEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SmsProviderRepository extends CrudRepository<SmsProviderEntity,Integer> {

    Optional<SmsProviderEntity> findTopBy();
}
