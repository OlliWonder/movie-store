package com.sber.java13.filmlibrary.repository;

import com.sber.java13.filmlibrary.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends GenericRepository<User> {
    User findUserByLogin(String login);
    
    User findUserByEmail(String email);
    
    User findUserByChangePasswordToken(String token);
    
    User findUserByLoginAndIsDeletedFalse(String login);
    
    @Query(nativeQuery = true,
            value = """
                 select u.*
                 from users u
                 where u.first_name ilike '%' || coalesce(:firstName, '%') || '%'
                 and u.last_name ilike '%' || coalesce(:lastName, '%') || '%'
                 and u.login ilike '%' || coalesce(:login, '%') || '%'
                  """)
    Page<User> searchUsers(String firstName,
                           String lastName,
                           String login,
                           Pageable pageable);
    
    @Query(nativeQuery = true,
            value = """
                 select email
                 from users u join orders o on u.id = o.user_id
                 where o.rent_date + o.rent_period >= now()
                 and u.is_deleted = false
                 """)
    List<String> getDelayedEmails();
}
