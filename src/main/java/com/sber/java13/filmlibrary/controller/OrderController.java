package com.sber.java13.filmlibrary.controller;

import com.sber.java13.filmlibrary.model.Order;
import com.sber.java13.filmlibrary.repository.OrderRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/orders")
@Tag(name = "Заказы", description = "Контроллер для работы с заказами")
public class OrderController extends GenericController<Order> {
    
    private final OrderRepository orderRepository;
    
    public OrderController(OrderRepository orderRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
    }
}
