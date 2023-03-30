package com.sber.java13.filmlibrary.constants;

public interface Errors {
    class Films {
        public static final String FILM_DELETE_ERROR = "Фильм не может быть удалён, так как у него есть активные аренды";
    }
    
    class Directors {
        public static final String DIRECTOR_DELETE_ERROR = "Режиссёр не может быть удалён, так как у его фильмов" +
                " есть активные аренды";
    }
    
    class Users {
        public static final String USER_FORBIDDEN_ERROR = "У вас нет прав просматривать информацию о пользователе";
    }
}
