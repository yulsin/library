package ru.itgirl.libraryproject.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itgirl.libraryproject.model.dto.UserDto;
import ru.itgirl.libraryproject.repository.RoleRepository;
import ru.itgirl.libraryproject.service.UserDetailsServiceImpl;
import ru.itgirl.libraryproject.service.UserServiceImpl;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@SecurityRequirement(name = "library-users")
@Slf4j
public class AuthUserRestController {
    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    @PostMapping("/signup")
    public String addUser(@RequestBody @Valid UserDto userDto) {
        userService.addUser(userDto);
        return "user saved";
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getLogin(), userDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsServiceImpl userDetailsServiceImpl = (UserDetailsServiceImpl) authentication.getPrincipal();
        List<String> roles = userDetailsServiceImpl.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                        .toList();
        log.info("User: {} logged in successfully", userDto.getLogin());
        return ResponseEntity
                .ok("User logged in successfully");
  }
}
