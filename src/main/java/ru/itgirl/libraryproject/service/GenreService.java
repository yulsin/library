package ru.itgirl.libraryproject.service;
import ru.itgirl.libraryproject.model.dto.GenreDto;

public interface GenreService {
    GenreDto getGenreById(Long id);
}
