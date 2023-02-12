package com.sber.java13.filmlibrary.service;

import com.sber.java13.filmlibrary.dto.UserDTO;
import com.sber.java13.filmlibrary.mapper.UserMapper;
import com.sber.java13.filmlibrary.model.User;
import com.sber.java13.filmlibrary.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends GenericService<User, UserDTO> {
    
    protected UserService(UserRepository userRepository, UserMapper userMapper) {
        super(userRepository, userMapper);
    }
}
