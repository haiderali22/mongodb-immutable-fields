package com.example.mongodbimmutablefields;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ImmutableUtil {

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
