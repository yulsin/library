package ru.itgirl.libraryproject.service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import ru.itgirl.libraryproject.model.dto.AuthorCreateDto;
import ru.itgirl.libraryproject.model.dto.AuthorDto;
import ru.itgirl.libraryproject.model.dto.AuthorUpdateDto;
import ru.itgirl.libraryproject.model.entity.Author;
import ru.itgirl.libraryproject.model.entity.Book;
import ru.itgirl.libraryproject.repository.AuthorRepository;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    public void testGetAuthorById() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        AuthorDto authorDto = authorService.getAuthorById(id);

        verify(authorRepository).findById(id);
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorByIdNotFound() {
        Long id = 1L;
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorById(id));
        verify(authorRepository).findById(id);
    }

    @Test
    public void testGetAuthorBySurnameV1() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);

        when(authorRepository.findAuthorBySurname(surname)).thenReturn(Optional.of(author));
        AuthorDto authorDto = authorService.getAuthorBySurnameV1(surname);

        verify(authorRepository).findAuthorBySurname(surname);
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorBySurnameV1NotFound() {
        String surname = "Doe";
        when(authorRepository.findAuthorBySurname(surname)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorBySurnameV1(surname));
        verify(authorRepository).findAuthorBySurname(surname);
    }

    @Test
    public void testGtAuthorBySurnameV2() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);

        when(authorRepository.findAuthorBySurnameBySql(surname)).thenReturn(Optional.of(author));
        AuthorDto authorDto = authorService.getAuthorBySurnameV2(surname);

        verify(authorRepository).findAuthorBySurnameBySql(surname);
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorBySurnameV2NotFound() {
        String surname = "Doe";
        when(authorRepository.findAuthorBySurnameBySql(surname)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorBySurnameV2(surname));
        verify(authorRepository).findAuthorBySurnameBySql(surname);
    }

    @Test
    public void testGetAuthorBySurnameV3() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);

        author.setName(name);
        author.setSurname(surname);
        when(authorRepository.findOne(any(Specification.class))).thenReturn(Optional.of(author));
        AuthorDto result = authorService.getAuthorBySurnameV3(name);
        Assertions.assertNotNull(result);
        assertEquals(surname, result.getSurname());
        verify(authorRepository).findOne(any(Specification.class));
    }

    @Test
    public void testGetAuthorByNameV3NotFound() {
        String surname = "Doe";
        when(authorRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> authorService.getAuthorBySurnameV3(surname));
        verify(authorRepository).findOne(any(Specification.class));
    }

    @Test
    public void testCreateAuthor() throws Exception {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);
        AuthorCreateDto authorCreateDto = new AuthorCreateDto(name, surname);

        when(authorRepository.save(any())).thenReturn(author);
        AuthorDto authorDto = authorService.createAuthor(authorCreateDto);

        verify(authorRepository).save(any());
        Assertions.assertEquals(authorDto.getName(), name);
        Assertions.assertEquals(authorDto.getSurname(), surname);
    }

    @Test
    public void testUpdateAuthor() {
        Long id = 1L;
        String name = "John";
        String surname = "Doe";
        Set<Book> books = new HashSet<>();
        Author author = new Author(id, name, surname, books);
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto(id, name, surname);

        when(authorRepository.findById(id)).thenReturn(Optional.of(author));
        when(authorRepository.save(any())).thenReturn(author);

        AuthorDto updatedAuthorDto = authorService.updateAuthor(authorUpdateDto);

        verify(authorRepository).findById(id);
        verify(authorRepository).save(any());
        Assertions.assertEquals(authorUpdateDto.getName(), name);
        Assertions.assertEquals(authorUpdateDto.getSurname(), surname);
    }

    @Test
    public void testUpdateAuthorNotFound() {
        Long id = 1L;
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto(id, "UpdatedJohn", "UpdatedDoe");
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> authorService.updateAuthor(authorUpdateDto));
        verify(authorRepository).findById(id);
    }

    @Test
    public void testDeleteAuthor() {
        Long id = 1L;
        authorService.deleteAuthor(id);
        verify(authorRepository).deleteById(id);
    }
}

