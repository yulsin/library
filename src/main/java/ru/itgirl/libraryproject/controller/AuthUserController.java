package ru.itgirl.libraryproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itgirl.libraryproject.model.dto.UserDto;
import ru.itgirl.libraryproject.service.UserServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthUserController {
    private final UserServiceImpl userService;

    @PostMapping("/signin")
    public String addUser(@RequestBody UserDto userDto) {
        userService.addUser(userDto);
        return "user saved";
    }
}
