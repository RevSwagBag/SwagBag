package com.revature.services;

import com.revature.dtos.ProductInfo;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.repositories.OrderRepository;
import com.revature.repositories.ProductRepository;
import com.revature.repositories.PurchaseRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PurchaseRepository purchaseRepository;

    public OrderService(OrderRepository orderRepository, PurchaseRepository purchaseRepository) {
        this.orderRepository = orderRepository;
        this.purchaseRepository = purchaseRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findAllByUserId(int userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public Optional<Order> findById(int orderId) {
        return orderRepository.findById(orderId);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }
    
    public List<Order> saveAll(List<Order> orderList) {
    	return orderRepository.saveAll(orderList);
    }

    public void delete(int orderId) {
        orderRepository.deleteById(orderId);
    }
}
