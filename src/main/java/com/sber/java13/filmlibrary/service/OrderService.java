package com.sber.java13.filmlibrary.service;

import com.sber.java13.filmlibrary.dto.FilmDTO;
import com.sber.java13.filmlibrary.dto.OrderDTO;
import com.sber.java13.filmlibrary.mapper.OrderMapper;
import com.sber.java13.filmlibrary.model.Order;
import com.sber.java13.filmlibrary.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OrderService extends GenericService<Order, OrderDTO> {
    private final FilmService filmService;
    private final OrderMapper orderMapper;
    
    protected OrderService(OrderRepository orderRepository, OrderMapper orderMapper, FilmService filmService,
                           OrderMapper orderMapper1) {
        super(orderRepository, orderMapper);
        this.filmService = filmService;
        this.orderMapper = orderMapper1;
    }
    
    public OrderDTO rentFilm(OrderDTO orderDTO) {
        FilmDTO filmDTO = filmService.getOne(orderDTO.getFilmId());
        long rentPeriod = orderDTO.getRentPeriod() != null ? orderDTO.getRentPeriod() : 14L;
        orderDTO.setRentDate(String.valueOf(LocalDate.now()));
        orderDTO.setRentPeriod((int) rentPeriod);
        return orderMapper.toDTO(repository.save(orderMapper.toEntity(orderDTO)));
    }
}
