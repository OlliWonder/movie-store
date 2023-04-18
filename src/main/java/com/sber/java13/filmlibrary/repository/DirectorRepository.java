package com.sber.java13.filmlibrary.repository;

import com.sber.java13.filmlibrary.model.Director;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorRepository extends GenericRepository<Director> {
    
    Page<Director> findAllByIsDeletedFalse(Pageable pageable);
    
    Page<Director> findAllByDirectorsFioContainsIgnoreCaseAndIsDeletedFalse(String fio, Pageable pageable);
    
    @Query("""
          select case when count(d) > 0 then false else true end
          from Director d join d.films f
                        join Order o on f.id = o.film.id
          where d.id = :directorId
          """)
    boolean checkDirectorForDeletion(final Long directorId);
}
