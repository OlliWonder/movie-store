package com.sber.java13.filmlibrary.service;

import com.sber.java13.filmlibrary.dto.OrderDTO;
import com.sber.java13.filmlibrary.mapper.OrderMapper;
import com.sber.java13.filmlibrary.model.Order;
import com.sber.java13.filmlibrary.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends GenericService<Order, OrderDTO> {
    
    protected OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        super(orderRepository, orderMapper);
    }
}
