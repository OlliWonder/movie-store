package com.sber.java13.filmlibrary.repository;

import com.sber.java13.filmlibrary.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User> {
    User findUserByLogin(String login);
    
    User findUserByEmail(String email);
}
