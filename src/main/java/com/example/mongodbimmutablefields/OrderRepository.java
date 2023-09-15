package com.example.mongodbimmutablefields;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.ListCrudRepository;

public interface OrderRepository extends MongoRepository<Order,String> {
}
