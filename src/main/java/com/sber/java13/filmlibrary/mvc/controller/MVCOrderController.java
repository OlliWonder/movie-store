package com.sber.java13.filmlibrary.mvc.controller;

import com.sber.java13.filmlibrary.dto.OrderDTO;
import com.sber.java13.filmlibrary.service.FilmService;
import com.sber.java13.filmlibrary.service.OrderService;
import com.sber.java13.filmlibrary.service.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Hidden
@Slf4j
@RequestMapping("/rent")
public class MVCOrderController {
    private final OrderService orderService;
    private final FilmService filmService;
    
    public MVCOrderController(OrderService orderService, FilmService filmService) {
        this.orderService = orderService;
        this.filmService = filmService;
    }
    
    @GetMapping("/film/{filmId}")
    public String rentFilm(@PathVariable Long filmId, Model model) {
        model.addAttribute("film", filmService.getOne(filmId));
        return "userFilms/rentFilm";
    }
    
    @PostMapping("/film")
    public String rentFilm(@ModelAttribute("rentFilmForm") OrderDTO orderDTO) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        orderDTO.setUserId(Long.valueOf(customUserDetails.getUserId()));
        orderService.rentFilm(orderDTO);
        return "redirect:/rent/user-films/" + customUserDetails.getUserId();
    }
    
    @GetMapping("/user-films/{id}")
    public String userFilms(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "5") int pageSize,
                            @PathVariable Long id, Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<OrderDTO> orderDTOPage = orderService.listUserRentFilms(id, pageRequest);
        model.addAttribute("rentFilms", orderDTOPage);
        return "userFilms/viewAllUserFilms";
    }
}
