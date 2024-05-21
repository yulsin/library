package ru.itgirl.libraryproject.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {
    private Long id;
    @Size(min = 2, max = 20)
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    private String login;
    @Size(min = 8, max = 15)
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^[A-Za-z0-9_.+-@!?]+$")
    private String password;
    private List<RoleDto> roles;
}
