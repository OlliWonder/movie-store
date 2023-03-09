package com.sber.java13.filmlibrary.mapper;

import com.sber.java13.filmlibrary.dto.UserDTO;
import com.sber.java13.filmlibrary.model.GenericModel;
import com.sber.java13.filmlibrary.model.User;
import com.sber.java13.filmlibrary.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserMapper extends GenericMapper<User, UserDTO> {
    
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    
    protected UserMapper(ModelMapper modelMapper, OrderRepository orderRepository) {
        super(modelMapper, User.class, UserDTO.class);
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
    }
    
    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(m -> m.skip(UserDTO::setOrdersIds)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(UserDTO.class, User.class)
                .addMappings(m -> m.skip(User::setOrders)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(User::setBirthDate)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(User::setCreatedWhen)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(UserDTO source, User destination) {
        if (!Objects.isNull(source.getOrdersIds())) {
            destination.setOrders(new HashSet<>(orderRepository.findAllById(source.getOrdersIds())));
        }
        else {
            destination.setOrders(Collections.emptySet());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(source.getBirthDate(), formatter);
        destination.setBirthDate(date);
        destination.setCreatedWhen(LocalDate.now());
    }
    
    @Override
    protected void mapSpecificFields(User source, UserDTO destination) {
        destination.setOrdersIds(Objects.isNull(source) || Objects.isNull(source.getOrders()) ? null
                : source.getOrders()
                .stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet()));
    }
}
