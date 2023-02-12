package com.sber.java13.filmlibrary.mapper;

import com.sber.java13.filmlibrary.dto.GenericDTO;
import com.sber.java13.filmlibrary.model.GenericModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public abstract class GenericMapper<E extends GenericModel, D extends GenericDTO> implements Mapper<E, D> {
    
    protected final ModelMapper modelMapper;
    private final Class<E> entityClass;
    private final Class<D> dtoClass;
    
    protected GenericMapper(ModelMapper modelMapper, Class<E> entityClass, Class<D> dtoClass) {
        this.modelMapper = modelMapper;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }
    
    @Override
    public E toEntity(D dto) {
        return Objects.isNull(dto) ? null : modelMapper.map(dto, entityClass);
    }
    
    @Override
    public List<E> toEntities(List<D> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }
    
    @Override
    public D toDTO(E entity) {
        return Objects.isNull(entity) ? null : modelMapper.map(entity, dtoClass);
    }
    
    @Override
    public List<D> toDTOs(List<E> entities) {
        return entities.stream().map(this::toDTO).toList();
    }
    
    Converter<D, E> toEntityConverter() {
        return context -> {
            D source = context.getSource();
            E destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }
    
    Converter<E, D> toDtoConverter() {
        return context -> {
            E source = context.getSource();
            D destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }
    
    abstract void mapSpecificFields(D source, E destination);
    
    abstract void mapSpecificFields(E source, D destination);
}
