package com.sber.java13.filmlibrary.service;

import com.sber.java13.filmlibrary.constants.MailConstants;
import com.sber.java13.filmlibrary.dto.RoleDTO;
import com.sber.java13.filmlibrary.dto.UserDTO;
import com.sber.java13.filmlibrary.exception.MyDeleteException;
import com.sber.java13.filmlibrary.mapper.UserMapper;
import com.sber.java13.filmlibrary.model.User;
import com.sber.java13.filmlibrary.repository.UserRepository;
import com.sber.java13.filmlibrary.utils.MailUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.sber.java13.filmlibrary.constants.UserRoleConstants.ADMIN;

@Service
public class UserService extends GenericService<User, UserDTO> {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JavaMailSender javaMailSender;
    
    protected UserService(UserRepository userRepository, UserMapper userMapper,
                          BCryptPasswordEncoder bCryptPasswordEncoder, JavaMailSender javaMailSender) {
        super(userRepository, userMapper);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.javaMailSender = javaMailSender;
    }
    
    public UserDTO getUserByLogin(final String login) {
        return mapper.toDTO(((UserRepository) repository).findUserByLogin(login));
    }
    
    public UserDTO getUserByEmail(final String email) {
        return mapper.toDTO(((UserRepository) repository).findUserByEmail(email));
    }
    
    public Boolean checkPassword(String password, UserDetails userDetails) {
        return bCryptPasswordEncoder.matches(password, userDetails.getPassword());
    }
    
    @Override
    public UserDTO create(UserDTO userDTO) {
        RoleDTO roleDTO = new RoleDTO();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (ADMIN.equalsIgnoreCase(userName)) {
            roleDTO.setId(2L);//фильмотекарь
        } else {
            roleDTO.setId(1L);//пользователь
        }
        userDTO.setRole(roleDTO);
        userDTO.setCreatedWhen(LocalDate.now());
        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        return mapper.toDTO(repository.save(mapper.toEntity(userDTO)));
    }
    
    public void sendChangePasswordEmail(final UserDTO userDTO) {
        UUID uuid = UUID.randomUUID();
        userDTO.setChangePasswordToken(uuid.toString());
        update(userDTO);
        SimpleMailMessage mailMessage = MailUtils.createEmailMessage(userDTO.getEmail(),
                MailConstants.MAIL_SUBJECT_FOR_REMEMBER_PASSWORD,
                MailConstants.MAIL_MESSAGE_FOR_REMEMBER_PASSWORD + uuid);
        javaMailSender.send(mailMessage);
    }
    
    public void changePassword(final String uuid,
                               final String password) {
        UserDTO user = mapper.toDTO(((UserRepository) repository).findUserByChangePasswordToken(uuid));
        user.setChangePasswordToken(null);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        update(user);
    }
    
    public Page<UserDTO> findUsers(UserDTO userDTO,
                                   Pageable pageable) {
        Page<User> users = ((UserRepository) repository).searchUsers(userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getLogin(),
                pageable);
        List<UserDTO> result = mapper.toDTOs(users.getContent());
        return new PageImpl<>(result, pageable, users.getTotalElements());
    }
    
    public List<String> getUserEmailsWithDelayedRentDate() {
        return ((UserRepository) repository).getDelayedEmails();
    }
    
    @Override
    public void delete(Long id) throws MyDeleteException {
        User user = repository.findById(id).orElseThrow(
                () -> new NotFoundException("Пользователя с заданным ID=" + id + " не существует"));
        markAsDeleted(user);
        repository.save(user);
    }
    
    public void restore(Long objectId) {
        User user = repository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Пользователя с заданным ID=" + objectId + " не существует"));
        unMarkAsDeleted(user);
        repository.save(user);
    }
}
