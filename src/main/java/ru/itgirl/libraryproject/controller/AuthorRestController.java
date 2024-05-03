package ru.itgirl.libraryproject.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itgirl.libraryproject.model.dto.AuthorCreateDto;
import ru.itgirl.libraryproject.model.dto.AuthorDto;
import ru.itgirl.libraryproject.model.dto.AuthorUpdateDto;
import ru.itgirl.libraryproject.service.AuthorService;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "library-users")
public class AuthorRestController {
    private final AuthorService authorService;

    @GetMapping("/author/{id}")
    AuthorDto getAuthorById(@PathVariable("id") Long id) {
        return authorService.getAuthorById(id);
    }

    @GetMapping("/author")
    AuthorDto getAuthorBySurname(@RequestParam("surname") String surname) {
        return authorService.getAuthorBySurnameV1(surname);
    }

    @GetMapping("/author/v2")
    AuthorDto getAuthorBySurnameV2(@RequestParam("surname") String surname) {
        return authorService.getAuthorBySurnameV2(surname);
    }

    @GetMapping("/author/v3")
    AuthorDto getAuthorBySurnameV3(@RequestParam("surname") String surname) {
        return authorService.getAuthorBySurnameV3(surname);
    }

    @PostMapping("/author/create")
    AuthorDto createAuthor(@RequestBody @Valid AuthorCreateDto authorCreateDto) throws Exception {
        return authorService.createAuthor(authorCreateDto);
    }

    @PutMapping("/author/update")
    AuthorDto updateAuthor(@RequestBody @Valid AuthorUpdateDto authorUpdateDto) {
        return authorService.updateAuthor(authorUpdateDto);
    }

    @DeleteMapping("/author/delete/{id}")
    void deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteAuthor(id);
    }
}