package ru.itgirl.libraryproject.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookUpdateDto {
    private Long id;
    @Size(min = 2, max = 20)
    @NotBlank(message = "Name must be specified")
    private String name;
    @Size(min = 3, max = 20)
    @NotBlank(message = "Name must be specified")
    private String genre;
}
