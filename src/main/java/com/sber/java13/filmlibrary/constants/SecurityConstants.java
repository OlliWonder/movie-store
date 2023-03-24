package com.sber.java13.filmlibrary.constants;

import java.util.List;

public interface SecurityConstants {
    List<String> RESOURCES_WHITE_LIST = List.of("/resources/**",
            "/js/**",
            "/css/**",
            "/",
            // -- Swagger UI v3 (OpenAPI)
            "/swagger-ui/**",
            "/webjars/bootstrap/5.2.3/**",
            "/v3/api-docs/**");
    
    List<String> FILMS_WHITE_LIST = List.of("/films",
            "/films/search",
            "/films/{id}");
    List<String> FILMS_PERMISSION_LIST = List.of("/films/add",
            "/films/update",
            "/films/delete",
            "/films/download/{filmId}");
    
    List<String> USERS_WHITE_LIST = List.of("/login",
            "/users/registration",
            "/users/remember-password",
            "/users/change-password");
    
    List<String> USERS_REST_WHITE_LIST = List.of("/users/auth");
    
    List<String> DIRECTORS_WHITE_LIST = List.of("/directors");
    
    List<String> DIRECTORS_PERMISSION_LIST = List.of("/directors/add",
            "/directors/update",
            "/directors/delete");
}
