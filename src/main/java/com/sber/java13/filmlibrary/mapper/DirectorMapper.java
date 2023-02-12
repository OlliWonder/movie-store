package com.sber.java13.filmlibrary.mapper;

import com.sber.java13.filmlibrary.dto.DirectorDTO;
import com.sber.java13.filmlibrary.model.Director;
import com.sber.java13.filmlibrary.model.GenericModel;
import com.sber.java13.filmlibrary.repository.FilmRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DirectorMapper extends GenericMapper<Director, DirectorDTO> {
    
    private final ModelMapper modelMapper;
    private final FilmRepository filmRepository;
    
    protected DirectorMapper(ModelMapper modelMapper, FilmRepository filmRepository) {
        super(modelMapper, Director.class, DirectorDTO.class);
        this.modelMapper = modelMapper;
        this.filmRepository = filmRepository;
    }
    
    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(Director.class, DirectorDTO.class)
                .addMappings(m -> m.skip(DirectorDTO::setFilmsIds)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(DirectorDTO.class, Director.class)
                .addMappings(m -> m.skip(Director::setFilms)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(DirectorDTO source, Director destination) {
        if (!Objects.isNull(source.getFilmsIds())) {
            destination.setFilms(new HashSet<>(filmRepository.findAllById(source.getFilmsIds())));
        }
        else {
            destination.setFilms(Collections.emptySet());
        }
    }
    
    @Override
    protected void mapSpecificFields(Director source, DirectorDTO destination) {
        destination.setFilmsIds(Objects.isNull(source) || Objects.isNull(source.getFilms()) ? null
                : source.getFilms().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet()));
    }
}
