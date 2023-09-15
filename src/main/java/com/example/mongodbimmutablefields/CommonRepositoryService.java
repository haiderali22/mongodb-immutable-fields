package com.example.mongodbimmutablefields;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class CommonRepositoryService {
    public <E extends  CommonEntity, R extends MongoRepository<E,String>> E update(final E entity, final R repository){
        CommonEntity old = repository.findById(entity.getId()).orElseThrow(() -> new IllegalArgumentException("Not found"));
        entity.setVersion(old.getVersion());

        setImmutableField(old,entity);

        return repository.save(entity);
    }

    private  <T> void setImmutableField(T oldObject, T newObject){
        Class<?> clazz = oldObject.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ImmutableField.class)) {
                try {
                    field.setAccessible(true);
                    Object oldValue = field.get(oldObject);
                    field.set(newObject, oldValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    // Handle the exception as needed
                }
            }
        }
    }

}
