package com.example.mongodbimmutablefields;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

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
        List<Field> fields = getAllFields(clazz);

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

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    fields.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

}
