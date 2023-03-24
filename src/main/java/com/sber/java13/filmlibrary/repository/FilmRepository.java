package com.sber.java13.filmlibrary.repository;

import com.sber.java13.filmlibrary.model.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends GenericRepository<Film> {
    
    @Query(nativeQuery = true,
            value = """
                 select f.*
                 from films f
                 left join films_directors fd on f.id = fd.film_id
                 join directors d on d.id = fd.director_id
                 where f.title ilike '%' || :title || '%'
                 and cast(f.genre as char) like coalesce(:genre,'%')
                 and d.directors_fio ilike '%' || :directors_fio || '%'
                      """)
    Page<Film> searchFilms(@Param(value = "genre") String genre,
                           @Param(value = "title") String title,
                           @Param(value = "directors_fio") String directorsFio,
                           Pageable pageable);
}
