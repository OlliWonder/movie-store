package com.sber.java13.filmlibrary.service;

import com.sber.java13.filmlibrary.dto.DirectorDTO;
import com.sber.java13.filmlibrary.mapper.DirectorMapper;
import com.sber.java13.filmlibrary.model.Director;
import com.sber.java13.filmlibrary.repository.DirectorRepository;
import org.springframework.stereotype.Service;

@Service
public class DirectorService extends GenericService<Director, DirectorDTO> {
    
    protected DirectorService(DirectorRepository directorRepository, DirectorMapper directorMapper) {
        super(directorRepository, directorMapper);
    }
}
