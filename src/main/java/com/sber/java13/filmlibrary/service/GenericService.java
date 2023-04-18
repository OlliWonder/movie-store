package com.sber.java13.filmlibrary.service;

import com.sber.java13.filmlibrary.dto.GenericDTO;
import com.sber.java13.filmlibrary.exception.MyDeleteException;
import com.sber.java13.filmlibrary.mapper.GenericMapper;
import com.sber.java13.filmlibrary.model.GenericModel;
import com.sber.java13.filmlibrary.repository.GenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public abstract class GenericService <T extends GenericModel, N extends GenericDTO> {
    
    protected final GenericRepository<T> repository;
    protected final GenericMapper<T, N> mapper;
    
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    protected GenericService(GenericRepository<T> repository, GenericMapper<T, N> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    public List<N> listAll() {
        return mapper.toDTOs(repository.findAll());
    }
    
    public Page<N> listAll(Pageable pageable) {
        Page<T> objects = repository.findAll(pageable);
        List<N> result = mapper.toDTOs(objects.getContent());
        return new PageImpl<>(result, pageable, objects.getTotalElements());
    }
    
    public Page<N> listAllNotDeleted(Pageable pageable) {
        Page<T> preResult = repository.findAllByIsDeletedFalse(pageable);
        List<N> result = mapper.toDTOs(preResult.getContent());
        return new PageImpl<>(result, pageable, preResult.getTotalElements());
    }
    
    public List<N> listAllNotDeleted() {
        return mapper.toDTOs(repository.findAllByIsDeletedFalse());
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
    
    public void markAsDeleted(GenericModel genericModel) {
        genericModel.setDeleted(true);
        genericModel.setDeletedWhen(LocalDateTime.now());
        genericModel.setDeletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
    }
    
    public void unMarkAsDeleted(GenericModel genericModel) {
        genericModel.setDeleted(false);
        genericModel.setDeletedWhen(null);
        genericModel.setDeletedBy(null);
    }
}
