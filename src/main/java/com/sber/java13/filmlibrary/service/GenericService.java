package com.sber.java13.filmlibrary.service;

import com.sber.java13.filmlibrary.dto.GenericDTO;
import com.sber.java13.filmlibrary.exception.MyDeleteException;
import com.sber.java13.filmlibrary.mapper.GenericMapper;
import com.sber.java13.filmlibrary.model.GenericModel;
import com.sber.java13.filmlibrary.repository.GenericRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
public abstract class GenericService <T extends GenericModel, N extends GenericDTO> {
    
    protected final GenericRepository<T> repository;
    protected final GenericMapper<T, N> mapper;
    
    protected GenericService(GenericRepository<T> repository, GenericMapper<T, N> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    public List<N> listAll() {
        return mapper.toDTOs(repository.findAll());
    }
    
    public N getOne(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Данные по заданному id: " + id + " не найдены")));
    }
    
    public N create(N object) {
        return mapper.toDTO(repository.save(mapper.toEntity(object)));
    }
    
    public N update(N object) {
        return mapper.toDTO(repository.save(mapper.toEntity(object)));
    }
    
    public void delete(Long id) throws MyDeleteException {
        repository.deleteById(id);
    }
}
