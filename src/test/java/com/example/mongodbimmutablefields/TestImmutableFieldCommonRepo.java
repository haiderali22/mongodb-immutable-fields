package com.example.mongodbimmutablefields;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TestImmutableFieldCommonRepo {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CommonRepositoryService commonRepositoryService;

    @Test
    public void testUpdate() {

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setAddress("address");
        order.setPhoneNumber("0584848484");
        Order orderSaved =  orderRepository.save(order);

        Order newOrder = orderSaved;
        newOrder.setAddress("address2");

        Order orderSaved2 = commonRepositoryService.update(newOrder, orderRepository);

        assertEquals("address", orderSaved2.getAddress());
    }

}
