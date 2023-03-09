package com.sber.java13.filmlibrary.service;

import com.sber.java13.filmlibrary.dto.DirectorDTO;
import com.sber.java13.filmlibrary.dto.DirectorWithFilmsDTO;
import com.sber.java13.filmlibrary.mapper.DirectorMapper;
import com.sber.java13.filmlibrary.mapper.DirectorWithFilmsMapper;
import com.sber.java13.filmlibrary.model.Director;
import com.sber.java13.filmlibrary.repository.DirectorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectorService extends GenericService<Director, DirectorDTO> {
    private final DirectorRepository directorRepository;
    private final DirectorWithFilmsMapper directorWithFilmsMapper;
    
    protected DirectorService(DirectorRepository directorRepository, DirectorMapper directorMapper,
                              DirectorWithFilmsMapper directorWithFilmsMapper) {
        super(directorRepository, directorMapper);
        this.directorRepository = directorRepository;
        this.directorWithFilmsMapper = directorWithFilmsMapper;
    }
    
    public List<DirectorWithFilmsDTO> getAllDirectorsWithFilms() {
        return directorWithFilmsMapper.toDTOs(directorRepository.findAll());
    }
}
