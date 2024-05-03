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
public class AuthorCreateDto {
    @Size(min = 2, max = 20)
    @NotBlank(message = "Name is required")
    private String name;
    @Size(min = 2, max = 20)
    @NotBlank(message = "Last name is required")
    private String surname;
}

