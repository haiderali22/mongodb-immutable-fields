package com.example.mongodbimmutablefields;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommonRepositoryService {
    public <E extends  CommonEntity, R extends MongoRepository<E,String>> E update(final E entity, final R repository){
        CommonEntity old = repository.findById(entity.getId()).orElseThrow(() -> new IllegalArgumentException("Not found"));
        entity.setVersion(old.getVersion());

//        try {
//            ImmutableUtil.setImmutableField(old,entity);
//        }catch (final Exception exception){
//            exception.printStackTrace();
//        }
        return repository.save(entity);
    }




}
