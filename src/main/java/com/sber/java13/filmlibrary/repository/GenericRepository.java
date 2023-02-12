package com.sber.java13.filmlibrary.repository;

import com.sber.java13.filmlibrary.model.GenericModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface GenericRepository<T extends GenericModel> extends JpaRepository<T, Long> {
}
