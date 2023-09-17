package com.example.mongodbimmutablefields;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EnableMongoAuditing
public class TestImmutableFieldCommonRepo {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CommonRepositoryService commonRepositoryService;

    @BeforeEach
    public void setUp() throws Exception {
        orderRepository.deleteAll();
    }

    @Test
    public void testUpdate() {

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setAddress("address");
        order.setPhoneNumber("0584848484");
        Order orderSaved =  orderRepository.save(order);

        Order newOrder = new Order();
        newOrder.setId(orderSaved.getId());
        newOrder.setAddress("address2");
        newOrder.setPhoneNumber("11111233");

        Order orderSaved2 = commonRepositoryService.update(newOrder, orderRepository);

        assertEquals(orderSaved.getCreatedDate(), orderSaved2.getCreatedDate());
        assertNotNull(orderSaved2.getLastModifiedDate());
        assertEquals("address", orderSaved2.getAddress());
        assertEquals("11111233", orderSaved2.getPhoneNumber());
    }

    @Test
    public void testSubFieldUpdate() {

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setAddress("address");
        order.setPhoneNumber("0584848484");

        Reference reference = new Reference();
        reference.setIdentifier(UUID.randomUUID().toString());
        reference.setHint("John");
        order.setOrderingPerson(reference);

        Order orderSaved =  orderRepository.save(order);

        Order newOrder = new Order();
        newOrder.setId(orderSaved.getId());
        newOrder.setAddress("address2");
        newOrder.setPhoneNumber("11111233");
        Reference reference2 = new Reference();
        reference2.setIdentifier(UUID.randomUUID().toString());
        reference2.setHint("John Doe");
        newOrder.setOrderingPerson(reference2);

        Order orderSaved2 = commonRepositoryService.update(newOrder, orderRepository);

        assertEquals(orderSaved.getCreatedDate(), orderSaved2.getCreatedDate());
        assertNotNull(orderSaved2.getLastModifiedDate());
        assertEquals("address", orderSaved2.getAddress());
        assertEquals("11111233", orderSaved2.getPhoneNumber());
        assertEquals(reference.getIdentifier(), orderSaved2.getOrderingPerson().getIdentifier());
        assertEquals("John Doe", orderSaved2.getOrderingPerson().getHint());
    }

}
