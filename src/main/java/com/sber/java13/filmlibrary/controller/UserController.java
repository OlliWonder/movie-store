package com.sber.java13.filmlibrary.controller;

import com.sber.java13.filmlibrary.dto.FilmDTO;
import com.sber.java13.filmlibrary.dto.OrderDTO;
import com.sber.java13.filmlibrary.dto.UserDTO;
import com.sber.java13.filmlibrary.model.User;
import com.sber.java13.filmlibrary.service.FilmService;
import com.sber.java13.filmlibrary.service.OrderService;
import com.sber.java13.filmlibrary.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Контроллер для работы с пользователями")
public class UserController extends GenericController<User, UserDTO> {
    
    private final UserService userService;
    private final OrderService orderService;
    private final FilmService filmService;
    
    public UserController(UserService userService, OrderService orderService, FilmService filmService) {
        super(userService);
        this.userService = userService;
        this.orderService = orderService;
        this.filmService = filmService;
    }
    
    @Operation(description = "Получить список всех фильмов пользователя", method = "getAllFilms")
    @RequestMapping(value = "/getAllFilms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<FilmDTO>> addFilm(@RequestParam(value = "id") Long id) {
        UserDTO userDTO = userService.getOne(id);
        Set<Long> ordersIds = userDTO.getOrdersIds();
        Set<FilmDTO> filmDTOs = ordersIds.stream()
                .map(orderService::getOne)
                .map(OrderDTO::getFilmId)
                .map(filmService::getOne)
                .collect(Collectors.toSet());
        
        return ResponseEntity.status(HttpStatus.OK).body(filmDTOs);
    }
}
