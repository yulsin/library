package ru.itgirl.libraryproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import ru.itgirl.libraryproject.model.dto.AuthorCreateDto;
import ru.itgirl.libraryproject.model.dto.AuthorDto;
import ru.itgirl.libraryproject.model.dto.AuthorUpdateDto;
import ru.itgirl.libraryproject.model.dto.BookDto;
import ru.itgirl.libraryproject.model.entity.Author;
import ru.itgirl.libraryproject.repository.AuthorRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public AuthorDto getAuthorById(Long id) {
        log.info("Try to find author by id {}", id);
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(author.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with id {} not found", id);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDto getAuthorBySurnameV1(String surname) {
        log.info("Try to find the author by surname: {}", surname);
        Optional<Author> optionalAuthor = authorRepository.findAuthorBySurname(surname);
        if (optionalAuthor.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(optionalAuthor.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with surname {} not found", surname);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDto getAuthorBySurnameV2(String surname) {
        log.info("Try to find the author by surname: {}", surname);
        Optional<Author> optionalAuthor = authorRepository.findAuthorBySurnameBySql(surname);
        if (optionalAuthor.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(optionalAuthor.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with name {} not found", surname);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDto getAuthorBySurnameV3(String surname) {
        Specification<Author> specification = Specification.where((Specification<Author>) (root, query, cb)
                -> cb.equal(root.get("surname"), surname));
        Optional<Author> optionalAuthor = authorRepository.findOne(specification);
        if (optionalAuthor.isPresent()) {
            AuthorDto authorDto = convertEntityToDto(optionalAuthor.get());
            log.info("Author: {}", authorDto.toString());
            return authorDto;
        } else {
            log.error("Author with name {} not found", surname);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public AuthorDto createAuthor(AuthorCreateDto authorCreateDto) throws Exception {
        log.info("Try to create the author: {}", authorCreateDto.toString());
        Optional<Author> optionalAuthor = authorRepository.findAuthorBySurname(authorCreateDto.getSurname());
        if (optionalAuthor.isPresent()) {
            log.error("The author already exists");
            throw new Exception ("The author already exists");
        } else {
            Author author = authorRepository.save(convertDtoToEntity(authorCreateDto));
            AuthorDto authorDto = convertEntityToDto(author);
            log.info("The author {} was created", authorDto.toString());
            return authorDto;
        }
    }

    @Override
    public AuthorDto updateAuthor(AuthorUpdateDto authorUpdateDto) {
        log.info("Try to update the author that has the next fields: {}", authorUpdateDto.toString());
        Optional<Author> optionalAuthor = authorRepository.findById(authorUpdateDto.getId());
        if (optionalAuthor.isEmpty()) {
            log.error("There is no author with such fields");
            throw new NoSuchElementException("There is no author with such fields");
        } else {
            Author author = optionalAuthor.get();
            author.setName(authorUpdateDto.getName());
            author.setSurname(authorUpdateDto.getSurname());
            Author savedAuthor = authorRepository.save(author);
            AuthorDto authorDto = convertEntityToDto(savedAuthor);
            log.info("The author {} was updated", authorDto.toString());
            return authorDto;
        }
    }

    @Override
    public void deleteAuthor(Long id) {
        log.info("Try to delete the author by id: {}", id);
        authorRepository.deleteById(id);
        log.info("The author with id: {} was deleted", id);
    }

    private Author convertDtoToEntity(AuthorCreateDto authorCreateDto) {
        return Author.builder()
                .name(authorCreateDto.getName())
                .surname(authorCreateDto.getSurname())
                .build();
    }

    private AuthorDto convertEntityToDto(Author author) {
        List<BookDto> bookDtoList = null;
        if (author.getBooks() != null) {
            bookDtoList = author.getBooks()
                    .stream()
                    .map(book -> BookDto.builder()
                            .genre(book.getGenre().getName())
                            .name(book.getName())
                            .id(book.getId())
                            .build())
                    .toList();
        }
        return AuthorDto.builder()
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .books(bookDtoList)
                .build();
    }
}

    /*

    private AuthorDto convertEntityToDto(Author author) {
        List<BookDto> bookDtoList = author.getBooks()
                .stream()
                .map(book -> BookDto.builder()
                        .genre(book.getGenre().getName())
                        .name(book.getName())
                        .id(book.getId())
                        .build()
                ).toList();
        return AuthorDto.builder()
                .books(bookDtoList)
                .id(author.getId())
                .name(author.getName())
                .surname(author.getSurname())
                .build();
    }

     */
