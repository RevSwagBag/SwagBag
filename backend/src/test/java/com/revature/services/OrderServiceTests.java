package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.revature.models.Order;
import com.revature.repositories.OrderRepository;

@SpringBootTest

public class OrderServiceTests {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    private Order o1 = new Order(1,null, LocalDateTime.now(), null);
    private Order o2 = new Order(1,null, LocalDateTime.now(), null);
    private Order o3 = new Order(1,null, LocalDateTime.now(), null);
    private Order o4 = new Order(1,null, LocalDateTime.now(), null);
    ArrayList<Order> orderList = new ArrayList<Order>();
    
    @BeforeEach
    public void setup(){
        
        orderList.add(o1);
        orderList.add(o2);
        orderList.add(o3);
        orderList.add(o4);
        Mockito.when(orderRepository.save(o1)).thenReturn(o1);
        Mockito.when(orderRepository.findAll()).thenReturn(orderList);   
          
    }

    @Test
    void testDelete() {

        Order o = orderService.save(o1);
        orderService.delete(o1.getOrderId()); 
        boolean actual = orderService.findById(o.getOrderId()).isPresent();
        assertEquals(false,actual);
    }

    @Test
    void testFindAll() {

        List<Order> expectedList = orderService.findAll();
        assertEquals(expectedList.size(), orderList.size());
    }

    @Test
    void testFindAllByUserId() {
        List<Order> expectedList = orderService.findAllByUserId(1);
        assertEquals(expectedList.size(), orderList.size());

    }

    @Test
    void testFindById() {
        Optional<Order> expectedList = orderService.findById(1);
        assertTrue(expectedList.isPresent());

    }

    @Test
    void testSave() {

    }

    @Test
    void testSaveAll() {

    }
}
