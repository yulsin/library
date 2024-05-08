package ru.itgirl.libraryproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itgirl.libraryproject.model.dto.AuthorDto;
import ru.itgirl.libraryproject.model.dto.BookCreateDto;
import ru.itgirl.libraryproject.model.dto.BookDto;
import ru.itgirl.libraryproject.model.dto.BookUpdateDto;
import ru.itgirl.libraryproject.model.entity.Book;
import ru.itgirl.libraryproject.model.entity.Genre;
import ru.itgirl.libraryproject.repository.BookRepository;
import ru.itgirl.libraryproject.repository.GenreRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public BookDto getBookById(Long id) {
        log.info("Try to find book by id {}", id);
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with id {} not found", id);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getBookByNameV1(String name) {
        log.info("Try to find the book by name: {}", name);
        Optional<Book> optionalBook = bookRepository.findBookByName(name);
        if (optionalBook.isPresent()) {
            BookDto bookDto = convertEntityToDto(optionalBook.get());
            log.info("Book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name {} not found", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getBookByNameV2(String name) {
        log.info("Try to find the book by name: {}", name);
        Optional<Book> optionalBook = bookRepository.findBookByNameBySql(name);
        if (optionalBook.isPresent()) {
            BookDto bookDto = convertEntityToDto(optionalBook.get());
            log.info("Book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name {} not found", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getBookByNameV3(String name) {
        log.info("Try to find the book by name: {}", name);
        Specification<Book> specification = Specification.where((Specification<Book>) (root, query, cb)
                -> cb.equal(root.get("name"), name));
        Optional<Book> optionalBook = bookRepository.findOne(specification);
        if (optionalBook.isPresent()) {
            BookDto bookDto = convertEntityToDto(optionalBook.get());
            log.info("Book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name {} not found", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto createBook(BookCreateDto bookCreateDto) throws Exception {
        log.info("Try to create the book: {}", bookCreateDto.toString());
        Optional<Book> existingBook = bookRepository.findBookByName(bookCreateDto.getName());
        if (existingBook.isPresent()) {
            log.error("The book already exists");
            throw new Exception("The book already exists");
        } else {
            Book book = bookRepository.save(convertDtoToEntity(bookCreateDto));
            BookDto bookDto = convertEntityToDto(book);
            log.info("The book {} was created", bookDto.toString());
            return bookDto;
        }
    }

    @Override
    public BookDto updateBook(BookUpdateDto bookUpdateDto) {
        log.info("Try to update the book that has the next fields: {}", bookUpdateDto.toString());
        Optional<Book> optionalBook = bookRepository.findById(bookUpdateDto.getId());
        if (optionalBook.isEmpty()) {
            log.error("There is no book with such fields");
            throw new NoSuchElementException("There is no book with such fields");
        }
        Book book = optionalBook.get();
        Optional<Genre> optionalGenre = genreRepository.findGenreByName(bookUpdateDto.getGenre());
        if (optionalGenre.isPresent()) {
            Genre genre = optionalGenre.get();
            book.setName(bookUpdateDto.getName());
            book.setGenre(genre);
        }
        Genre genre = new Genre();
        genre.setName(bookUpdateDto.getGenre());
        book.setName(bookUpdateDto.getName());
        book.setGenre(genre);
        Book savedBook = bookRepository.save(book);
        BookDto bookDto = convertEntityToDto(savedBook);
        log.info("The book {} was updated", bookDto.toString());
        return bookDto;
    }

    @Override
    public void deleteBook(Long id) {
        log.info("Try to delete the book by id: {}", id);
        bookRepository.deleteById(id);
        log.info("The book with id: {} was deleted", id);
    }

    private Book convertDtoToEntity(BookCreateDto bookCreateDto) {
        Optional<Genre> optionalGenre = genreRepository.findGenreByName(bookCreateDto.getGenre());
        if (optionalGenre.isPresent()) {
            return Book.builder()
                    .name(bookCreateDto.getName())
                    .genre(optionalGenre.get())
                    .build();
        } return Book.builder()
                .name(bookCreateDto.getName())
                .genre(new Genre())
                .build();
    }

    private BookDto convertEntityToDto(Book book) {
        List<AuthorDto> authorDtoList = null;
        if(book.getAuthors() != null) {
            authorDtoList = book.getAuthors()
                    .stream()
                    .map(author -> AuthorDto.builder()
                            .surname(author.getSurname())
                            .name(author.getName())
                            .id(author.getId())
                            .build())
                    .toList();
        }
        return BookDto.builder()
                .id(book.getId())
                .name(book.getName())
                .genre(book.getGenre().getName())
                .authors(authorDtoList)
                .build();
    }
}
