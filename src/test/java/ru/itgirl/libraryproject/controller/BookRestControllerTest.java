package ru.itgirl.libraryproject.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.itgirl.libraryproject.model.dto.BookCreateDto;
import ru.itgirl.libraryproject.model.dto.BookDto;
import ru.itgirl.libraryproject.model.dto.BookUpdateDto;
import ru.itgirl.libraryproject.service.BookService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetBookById() throws Exception {
        Long bookId = 1L;
        String genre = "Рассказ";
        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);
        bookDto.setName("Старик и море");
        bookDto.setGenre(genre);

        when(bookService.getBookById(bookId)).thenReturn(bookDto);

        mockMvc.perform(get("/book/{id}", bookId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookDto.getId()))
                .andExpect(jsonPath("$.name").value(bookDto.getName()))
                .andExpect(jsonPath("$.genre").value(bookDto.getGenre()));
    }

    @Test
    public void getBookByNameV1() throws Exception {
        Long bookId = 1L;
        String bookName = "Старик и море";
        String genre = "Рассказ";
        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);
        bookDto.setName(bookName);
        bookDto.setGenre(genre);

        when(bookService.getBookByNameV1(bookName)).thenReturn(bookDto);

        mockMvc.perform(get("/book/v1")
                        .param("name", bookName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookDto.getId()))
                .andExpect(jsonPath("$.name").value(bookDto.getName()))
                .andExpect(jsonPath("$.genre").value(bookDto.getGenre()));
    }

    @Test
    public void getBookByNameV2() throws Exception {
        Long bookId = 1L;
        String bookName = "Старик и море";
        String genre = "Рассказ";
        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);
        bookDto.setName(bookName);
        bookDto.setGenre(genre);

        when(bookService.getBookByNameV2(bookName)).thenReturn(bookDto);

        mockMvc.perform(get("/book/v2")
                        .param("name", bookName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookDto.getId()))
                .andExpect(jsonPath("$.name").value(bookDto.getName()))
                .andExpect(jsonPath("$.genre").value(bookDto.getGenre()));
    }

    @Test
    public void getBookByNameV3() throws Exception {
        Long bookId = 1L;
        String bookName = "Старик и море";
        String genre = "Рассказ";
        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);
        bookDto.setName(bookName);
        bookDto.setGenre(genre);

        when(bookService.getBookByNameV3(bookName)).thenReturn(bookDto);

        mockMvc.perform(get("/book/v3")
                        .param("name", bookName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookDto.getId()))
                .andExpect(jsonPath("$.name").value(bookDto.getName()))
                .andExpect(jsonPath("$.genre").value(bookDto.getGenre()));
    }

    @Test
    void testCreateBook() throws Exception {
        String bookName = "Старик и море";
        String genre = "Рассказ";
        BookCreateDto bookCreateDto = new BookCreateDto();
        bookCreateDto.setName(bookName);
        bookCreateDto.setGenre(genre);
        BookDto createdBookDto = new BookDto();
        createdBookDto.setId(1L);
        createdBookDto.setName(bookCreateDto.getName());
        createdBookDto.setGenre(bookCreateDto.getGenre());
        when(bookService.createBook(any(BookCreateDto.class))).thenReturn(createdBookDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/book/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Старик и море"))
                .andExpect(jsonPath("$.genre").value("Рассказ"));
    }

    @Test
    void testUpdateBook() throws Exception {
        String bookName = "Старик и море";
        String genre = "Рассказ";
        BookUpdateDto authorUpdateDto = new BookUpdateDto();
        authorUpdateDto.setId(1L);
        authorUpdateDto.setName(bookName);
        authorUpdateDto.setGenre(genre);
        BookDto updatedBookDto = new BookDto();
        updatedBookDto.setId(1L);
        updatedBookDto.setName(authorUpdateDto.getName());
        updatedBookDto.setGenre(authorUpdateDto.getGenre());

        when(bookService.updateBook(any(BookUpdateDto.class))).thenReturn(updatedBookDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/book/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Старик и море"))
                .andExpect(jsonPath("$.genre").value("Рассказ"));
    }

    @Test
    void testDeleteBook() throws Exception {
        Long bookId = 1L;
        mockMvc.perform(delete("/book/delete/{id}", bookId))
                .andExpect(status().isOk());
        Mockito.verify(bookService, Mockito.times(1)).deleteBook(bookId);
    }
}
