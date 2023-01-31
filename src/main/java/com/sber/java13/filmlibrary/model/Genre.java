package com.sber.java13.filmlibrary.model;

public enum Genre {
    FANTASY("Фантастика"),
    COMEDY("Комедия"),
    DRAMA("Драма"),
    MELODRAMA("Мелодрама"),
    THRILLER("Триллер"),
    HORROR("Ужасы"),
    ADVENTURES("Приключения"),
    BLOCKBUSTER("Боевик"),
    MULTIPLICATION("Мультфильм");
    private final String genreTextDisplay;
    
    Genre(String genreName) {
        this.genreTextDisplay = genreName;
    }
    
    public String getGenreTextDisplay() {
        return this.genreTextDisplay;
    }
}
