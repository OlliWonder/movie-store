package com.sber.java13.filmlibrary.mapper;

import com.sber.java13.filmlibrary.dto.OrderDTO;
import com.sber.java13.filmlibrary.model.Order;
import com.sber.java13.filmlibrary.repository.FilmRepository;
import com.sber.java13.filmlibrary.repository.UserRepository;
import com.sber.java13.filmlibrary.service.FilmService;
import com.sber.java13.filmlibrary.utils.DateFormatter;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

@Component
public class OrderMapper extends GenericMapper<Order, OrderDTO> {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final FilmService filmService;
    
    protected OrderMapper(ModelMapper modelMapper, FilmRepository filmRepository, UserRepository userRepository,
                          FilmService filmService) {
        super(modelMapper, Order.class, OrderDTO.class);
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
        this.filmService = filmService;
    }
    
    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Order.class, OrderDTO.class)
                .addMappings(m -> m.skip(OrderDTO::setUserId)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderDTO::setFilmId)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderDTO::setFilmDTO)).setPostConverter(toDtoConverter());
        
        modelMapper.createTypeMap(OrderDTO.class, Order.class)
                .addMappings(m -> m.skip(Order::setFilm)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(OrderDTO source, Order destination) {
        destination.setFilm(filmRepository.findById(source.getFilmId())
                .orElseThrow(() -> new NotFoundException("Фильм не найден")));
        destination.setUser(userRepository.findById(source.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден")));
    }
    
    @Override
    protected void mapSpecificFields(Order source, OrderDTO destination) {
        destination.setUserId(source.getUser().getId());
        destination.setFilmId(source.getFilm().getId());
        destination.setFilmDTO(filmService.getOne(source.getFilm().getId()));
    }
}
