package com.sber.java13.filmlibrary.controller;

import com.sber.java13.filmlibrary.dto.FilmDTO;
import com.sber.java13.filmlibrary.dto.OrderDTO;
import com.sber.java13.filmlibrary.dto.UserDTO;
import com.sber.java13.filmlibrary.model.Order;
import com.sber.java13.filmlibrary.service.FilmService;
import com.sber.java13.filmlibrary.service.OrderService;
import com.sber.java13.filmlibrary.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@Tag(name = "Заказы", description = "Контроллер для работы с заказами")
public class OrderController extends GenericController<Order, OrderDTO> {
    
    private final OrderService orderService;
    private final FilmService filmService;
    private final UserService userService;
    
    public OrderController(OrderService orderService, FilmService filmService, UserService userService) {
        super(orderService);
        this.orderService = orderService;
        this.filmService = filmService;
        this.userService = userService;
    }
    
    @Operation(description = "Взять фильм в аренду", method = "create")
    @RequestMapping(value = "/addOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> create(@RequestBody OrderDTO newEntity) {
        FilmDTO filmDTO = filmService.getOne(newEntity.getFilmId());
        UserDTO userDTO = userService.getOne(newEntity.getUserId());
        filmDTO.getOrdersIds().add(newEntity.getId());
        userDTO.getOrdersIds().add(newEntity.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(newEntity));
    }
}
