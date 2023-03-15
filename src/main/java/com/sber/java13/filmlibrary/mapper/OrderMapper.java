package com.sber.java13.filmlibrary.mapper;

import com.sber.java13.filmlibrary.dto.OrderDTO;
import com.sber.java13.filmlibrary.model.Order;
import com.sber.java13.filmlibrary.repository.FilmRepository;
import com.sber.java13.filmlibrary.repository.UserRepository;
import com.sber.java13.filmlibrary.utils.DateFormatter;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

@Component
public class OrderMapper extends GenericMapper<Order, OrderDTO> {
    
    private final ModelMapper modelMapper;
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    
    protected OrderMapper(ModelMapper modelMapper, FilmRepository filmRepository, UserRepository userRepository) {
        super(modelMapper, Order.class, OrderDTO.class);
        this.modelMapper = modelMapper;
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
    }
    
    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Order.class, OrderDTO.class)
                .addMappings(m -> m.skip(OrderDTO::setUserId)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderDTO::setFilmId)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(OrderDTO.class, Order.class)
                .addMappings(m -> m.skip(Order::setRentDate)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(OrderDTO source, Order destination) {
        destination.setFilm(filmRepository.findById(source.getFilmId())
                .orElseThrow(() -> new NotFoundException("Фильм не найден")));
        destination.setUser(userRepository.findById(source.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден")));
        destination.setRentDate(DateFormatter.formatStringToDate(source.getRentDate()));
    }
    
    @Override
    protected void mapSpecificFields(Order source, OrderDTO destination) {
        destination.setUserId(source.getUser().getId());
        destination.setFilmId(source.getFilm().getId());
    }
}
