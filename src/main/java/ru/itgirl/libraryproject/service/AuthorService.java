package ru.itgirl.libraryproject.service;

import ru.itgirl.libraryproject.model.dto.AuthorCreateDto;
import ru.itgirl.libraryproject.model.dto.AuthorDto;
import ru.itgirl.libraryproject.model.dto.AuthorUpdateDto;
import java.util.List;

public interface AuthorService {
    List<AuthorDto> getAllAuthors();
    AuthorDto getAuthorById(Long id);
    AuthorDto getAuthorBySurnameV1(String surname);
    AuthorDto getAuthorBySurnameV2(String surname);
    AuthorDto getAuthorBySurnameV3(String surname);
    AuthorDto createAuthor(AuthorCreateDto authorCreateDto) throws Exception;
    AuthorDto updateAuthor(AuthorUpdateDto authorUpdateDto);
    void deleteAuthor(Long id);
}
