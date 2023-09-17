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

        try {
            setImmutableField(old,entity);
        }catch (final Exception exception){
            exception.printStackTrace();
        }
        return repository.save(entity);
    }


    public static <T> void setImmutableField(T oldObject, T newObject) {
        Class<?> clazz = oldObject.getClass();
        List<Field> fields = getAllFields(clazz);
        for (Field field : fields) {
            if (field.isAnnotationPresent(ImmutableField.class)) {
                    ReflectionUtils.makeAccessible(field);
                    Object oldValue = ReflectionUtils.getField(field, oldObject);
                    ReflectionUtils.setField(field, newObject, oldValue);
            } else if (!field.getType().isPrimitive() && !field.getType().getName().startsWith("java.lang")) {
                // Recursively check nested objects
                ReflectionUtils.makeAccessible(field);
                Object oldFieldValue = ReflectionUtils.getField(field, oldObject);
                Object newFieldValue = ReflectionUtils.getField(field, newObject);
                if (oldFieldValue != null && newFieldValue != null) {
                    setImmutableField(oldFieldValue, newFieldValue);
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
