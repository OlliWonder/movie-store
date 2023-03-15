package com.sber.java13.filmlibrary.service;

import com.sber.java13.filmlibrary.dto.RoleDTO;
import com.sber.java13.filmlibrary.dto.UserDTO;
import com.sber.java13.filmlibrary.mapper.UserMapper;
import com.sber.java13.filmlibrary.model.User;
import com.sber.java13.filmlibrary.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService extends GenericService<User, UserDTO> {
    
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    
    protected UserService(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(userRepository, userMapper);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    public UserDTO getUserByLogin(final String login) {
        return mapper.toDTO(((UserRepository) repository).findUserByLogin(login));
    }
    
    public UserDTO getUserByEmail(final String email) {
        return mapper.toDTO(((UserRepository) repository).findUserByEmail(email));
    }
    
    @Override
    public UserDTO create(UserDTO userDTO) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        userDTO.setRole(roleDTO);
        userDTO.setCreatedWhen(LocalDate.now());
        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        return mapper.toDTO(repository.save(mapper.toEntity(userDTO)));
    }
}
