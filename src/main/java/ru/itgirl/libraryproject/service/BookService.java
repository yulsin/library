package ru.itgirl.libraryproject.service;
import ru.itgirl.libraryproject.model.dto.BookCreateDto;
import ru.itgirl.libraryproject.model.dto.BookDto;
import ru.itgirl.libraryproject.model.dto.BookUpdateDto;
import java.util.List;

public interface BookService {
    List<BookDto> getAllBooks();
    BookDto getBookById(Long id);
    BookDto getBookByNameV1(String name);
    BookDto getBookByNameV2(String name);
    BookDto getBookByNameV3(String name);
    BookDto createBook(BookCreateDto bookCreateDto) throws Exception;
    BookDto updateBook(BookUpdateDto bookUpdateDto);
    void deleteBook(Long id);
}
