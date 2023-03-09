package com.sber.java13.filmlibrary.mapper;

import com.sber.java13.filmlibrary.dto.FilmWithDirectorsDTO;
import com.sber.java13.filmlibrary.model.Film;
import com.sber.java13.filmlibrary.model.GenericModel;
import com.sber.java13.filmlibrary.repository.DirectorRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class FilmWithDirectorsMapper extends GenericMapper<Film, FilmWithDirectorsDTO> {
    
    private final DirectorRepository directorRepository;
    
    protected FilmWithDirectorsMapper(ModelMapper modelMapper, DirectorRepository directorRepository) {
        super(modelMapper, Film.class, FilmWithDirectorsDTO.class);
        this.directorRepository = directorRepository;
    }
    
    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(Film.class, FilmWithDirectorsDTO.class)
                .addMappings(m -> m.skip(FilmWithDirectorsDTO::setDirectorsIds)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(FilmWithDirectorsDTO.class, Film.class)
                .addMappings(m -> m.skip(Film::setDirectors)).setPostConverter(toEntityConverter());
    }
    
    @Override
    protected void mapSpecificFields(FilmWithDirectorsDTO source, Film destination) {
        destination.setDirectors(new HashSet<>(directorRepository.findAllById(source.getDirectorsIds())));
    }
    
    @Override
    protected void mapSpecificFields(Film source, FilmWithDirectorsDTO destination) {
        destination.setDirectorsIds(Objects.isNull(source) || Objects.isNull(source.getId()) ? null
                : source.getDirectors()
                .stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet()));
    }
}
