package ru.itgirl.libraryproject.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import ru.itgirl.libraryproject.model.dto.*;
import ru.itgirl.libraryproject.model.entity.Author;
import ru.itgirl.libraryproject.model.entity.Book;
import ru.itgirl.libraryproject.model.entity.Genre;
import ru.itgirl.libraryproject.repository.BookRepository;
import ru.itgirl.libraryproject.repository.GenreRepository;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private GenreRepository genreRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void testGetBookById() {
        Long id = 1L;
        String name = "Book";
        Genre genre = new Genre();
        Set<Author> authors = new HashSet<>();
        Book book = new Book(id, name, genre, authors);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        BookDto bookDto = bookService.getBookById(id);

        verify(bookRepository).findById(id);
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), book.getGenre().getName());
    }

    @Test
    public void testGetBookByIdNotFound() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.getBookById(id));
        verify(bookRepository).findById(id);
    }

    @Test
    public void testGetBookByNameV1() {
        Long id = 1L;
        String name = "Book";
        Genre genre = new Genre();
        Set<Author> authors = new HashSet<>();
        Book book = new Book(id, name, genre, authors);

        when(bookRepository.findBookByName(name)).thenReturn(Optional.of(book));
        BookDto bookDto = bookService.getBookByNameV1(name);

        verify(bookRepository).findBookByName(name);
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), book.getGenre().getName());
    }

    @Test
    public void testGetBookByNameV1NotFound() {
        String name = "Book";
        when(bookRepository.findBookByName(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.getBookByNameV1(name));
        verify(bookRepository).findBookByName(name);
    }

    @Test
    public void testGetBookByNameV2() {
        Long id = 1L;
        String name = "Book";
        Genre genre = new Genre();
        Set<Author> authors = new HashSet<>();
        Book book = new Book(id, name, genre, authors);

        when(bookRepository.findBookByNameBySql(name)).thenReturn(Optional.of(book));
        BookDto bookDto = bookService.getBookByNameV2(name);

        verify(bookRepository).findBookByNameBySql(name);
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), book.getGenre().getName());
    }

    @Test
    public void testGetBookByNameV2NotFound() {
        String name = "Book";
        when(bookRepository.findBookByNameBySql(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.getBookByNameV2(name));
        verify(bookRepository).findBookByNameBySql(name);
    }

    @Test
    public void testGetBookByNameV3() {
        Long id = 1L;
        String name = "Book";
        Genre genre = new Genre();
        Set<Author> authors = new HashSet<>();
        Book book = new Book(id, name, genre, authors);

        book.setName(name);
        book.setGenre(genre);
        when(bookRepository.findOne(any(Specification.class))).thenReturn(Optional.of(book));
        BookDto result = bookService.getBookByNameV3(name);
        Assertions.assertNotNull(result);
        assertEquals(name, result.getName());
        verify(bookRepository).findOne(any(Specification.class));
    }

    @Test
    public void testGetBookByNameV3NotFound() {
        String name = "Book";
        when(bookRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> bookService.getBookByNameV3(name));
        verify(bookRepository).findOne(any(Specification.class));
    }

    @Test
    public void testCreateBook() throws Exception {
        BookCreateDto bookCreateDto = new BookCreateDto();
        bookCreateDto.setName("eBook");
        bookCreateDto.setGenre("bookGenre");

        Book book = new Book();
        book.setName(bookCreateDto.getName());
        book.setGenre(new Genre());

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto bookDto = bookService.createBook(bookCreateDto);

        Assertions.assertEquals(bookDto.getName(), bookCreateDto.getName());
    }

    @Test
    public void testUpdateBook() {
        Long bookId = 1L;
        BookUpdateDto bookUpdateDto = new BookUpdateDto();
        bookUpdateDto.setId(bookId);
        bookUpdateDto.setName("eBook");
        bookUpdateDto.setGenre("bookGenre");

        Book book = new Book();
        book.setName("Book");
        Genre genre = new Genre();
        genre.setName("bookGenre");
        book.setGenre(genre);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookDto updatedBookDto = bookService.updateBook(bookUpdateDto);
        Assertions.assertNotNull(updatedBookDto);

        Assertions.assertEquals(bookUpdateDto.getName(), updatedBookDto.getName());
        Assertions.assertEquals(bookUpdateDto.getGenre(), updatedBookDto.getGenre());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    public void testUpdateBookNotFound() {
        Long id = 1L;
        BookUpdateDto bookUpdateDto = new BookUpdateDto(id, "Book", "bookGenre");
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> bookService.updateBook(bookUpdateDto));
        verify(bookRepository).findById(id);
    }

    @Test
    public void testDeleteBook() {
        Long id = 1L;
        bookService.deleteBook(id);
        verify(bookRepository).deleteById(id);
    }
}
