package ru.itgirl.libraryproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.itgirl.libraryproject.model.dto.AuthorCreateDto;
import ru.itgirl.libraryproject.model.dto.AuthorDto;
import ru.itgirl.libraryproject.model.dto.AuthorUpdateDto;
import ru.itgirl.libraryproject.service.AuthorService;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthorRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthorService authorService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAuthorById() throws Exception {
        Long authorId = 1L;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName("Александр");
        authorDto.setSurname("Пушкин");

        when(authorService.getAuthorById(authorId)).thenReturn(authorDto);

        mockMvc.perform(get("/author/{id}", authorId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authorDto.getId()))
                .andExpect(jsonPath("$.name").value(authorDto.getName()))
                .andExpect(jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void getAuthorBySurnameV1() throws Exception {
        Long authorId = 1L;
        String authorSurname = "Пушкин";
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName("Александр");
        authorDto.setSurname(authorSurname);

        when(authorService.getAuthorBySurnameV1(authorSurname)).thenReturn(authorDto);

        mockMvc.perform(get("/author/v1")
                        .param("surname", authorSurname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authorDto.getId()))
                .andExpect(jsonPath("$.name").value(authorDto.getName()))
                .andExpect(jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void getAuthorBySurnameV2() throws Exception {
        Long authorId = 1L;
        String authorSurname = "Пушкин";
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName("Александр");
        authorDto.setSurname(authorSurname);

        when(authorService.getAuthorBySurnameV2(authorSurname)).thenReturn(authorDto);

        mockMvc.perform(get("/author/v2")
                        .param("surname", authorSurname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authorDto.getId()))
                .andExpect(jsonPath("$.name").value(authorDto.getName()))
                .andExpect(jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void getAuthorBySurnameV3() throws Exception {
        Long authorId = 1L;
        String authorSurname = "Пушкин";
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);
        authorDto.setName("Александр");
        authorDto.setSurname(authorSurname);

        when(authorService.getAuthorBySurnameV3(authorSurname)).thenReturn(authorDto);

        mockMvc.perform(get("/author/v3")
                        .param("surname", authorSurname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authorDto.getId()))
                .andExpect(jsonPath("$.name").value(authorDto.getName()))
                .andExpect(jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    void testCreateAuthor() throws Exception {
        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("Александр");
        authorCreateDto.setSurname("Пушкин");
        AuthorDto createdAuthorDto = new AuthorDto();
        createdAuthorDto.setId(1L);
        createdAuthorDto.setName(authorCreateDto.getName());
        createdAuthorDto.setSurname(authorCreateDto.getSurname());
        when(authorService.createAuthor(any(AuthorCreateDto.class))).thenReturn(createdAuthorDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/author/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Александр"))
                .andExpect(jsonPath("$.surname").value("Пушкин"));
    }

    @Test
    void testUpdateAuthor() throws Exception {
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto();
        authorUpdateDto.setId(1L);
        authorUpdateDto.setName("Александр");
        authorUpdateDto.setSurname("Пушкин");
        AuthorDto updatedAuthorDto = new AuthorDto();
        updatedAuthorDto.setId(1L);
        updatedAuthorDto.setName(authorUpdateDto.getName());
        updatedAuthorDto.setSurname(authorUpdateDto.getSurname());

        when(authorService.updateAuthor(any(AuthorUpdateDto.class))).thenReturn(updatedAuthorDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/author/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Александр"))
                .andExpect(jsonPath("$.surname").value("Пушкин"));
    }

    @Test
    void testDeleteAuthor() throws Exception {
        Long authorId = 1L;
        mockMvc.perform(delete("/author/delete/{id}", authorId))
                .andExpect(status().isOk());
        Mockito.verify(authorService, Mockito.times(1)).deleteAuthor(authorId);
    }
}