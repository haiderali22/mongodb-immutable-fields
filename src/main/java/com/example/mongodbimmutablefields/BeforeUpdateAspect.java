package com.example.mongodbimmutablefields;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class BeforeUpdateAspect {

    private final MongoTemplate mongoTemplate;

    @Before("execution(* org.springframework.data.mongodb.core.MongoTemplate.insert(..))")
    public void beforeInsert(JoinPoint joinPoint) {
        // Extract the entity being updated from the arguments
        Object[] args = joinPoint.getArgs();
        if (args.length >= 3 && args[2] instanceof Class) {
            Class<?> entityClass = (Class<?>) args[2];

            // Now you have the entity class, you can do further processing
            System.out.println("Before MongoTemplate.insert operation for entity: " + entityClass.getName());
        }
    }

    @Before("execution(* org.springframework.data.mongodb.core.MongoTemplate.update(..))")
    public void beforeUpdate(JoinPoint joinPoint) {
        // Extract the entity being updated from the arguments
        Object[] args = joinPoint.getArgs();
        if (args.length >= 3 && args[2] instanceof Class) {
            Class<?> entityClass = (Class<?>) args[2];

            // Now you have the entity class, you can do further processing
            System.out.println("Before MongoTemplate.update operation for entity: " + entityClass.getName());
        }
    }

    @Before("execution(* org.springframework.data.mongodb.core.MongoTemplate.save(..))")
    public void beforeSave(JoinPoint joinPoint) {
        // Extract the entity being updated from the arguments
        Object[] args = joinPoint.getArgs();
        if (args.length >= 1 && args[0] instanceof CommonEntity) {
            Object entity = args[0];
            String id = ((CommonEntity)entity).getId();
            Object oldValue = mongoTemplate.findById(id , entity.getClass());
            if(oldValue != null){
                ImmutableUtil.setImmutableField(oldValue,entity);
            }
            // Now you have the entity class, you can do further processing
            System.out.println("Before MongoTemplate.save operation for entity: " + entity.getClass().getName());
        }
    }
}