package ru.itgirl.libraryproject.service;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itgirl.libraryproject.model.dto.*;
import ru.itgirl.libraryproject.model.entity.Role;
import ru.itgirl.libraryproject.model.entity.User;
import ru.itgirl.libraryproject.repository.RoleRepository;
import ru.itgirl.libraryproject.repository.UserRepository;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SecurityRequirement(name = "library-users")
@Slf4j
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        log.info("Try to find a user by name {}", login);
        Optional<User> user = userRepository.findUserByLogin(login);
        if (user.isPresent()) {
            UserDetails userDetails = UserDetailsServiceImpl.builder()
                    .login(user.get().getLogin())
                    .password(user.get().getPassword())
                    .authorities(user.get().getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority(role.getRole())).toList())
                    .build();
            return userDetails;
        } else {
            log.error("User with name {} not found.", login);
            throw new UsernameNotFoundException(login + " not found");
        }
    }

    public UserDto addUser(UserDto userDto) {
        User user = userRepository.save(convertDtoToEntity(userDto));
        return userDto;
    }

    private User convertDtoToEntity(UserDto userDto) {
        Set<Role> roleList = userDto.getRoles()
                .stream()
                .map(role -> Role.builder()
                        .role(role.getRole())
                        .build())
        .collect(Collectors.toSet());
        return User.builder()
                .login(userDto.getLogin())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(roleList)
                .build();
    }
/*
    private UserDto convertEntityToDto(User user) {
        List<RoleDto> roleDtoList = null;
        if(user.getRoles() != null) {
            roleDtoList = user.getRoles()
                    .stream()
                    .map(role -> RoleDto.builder()
                            .role(role.getRole())
                            .build())
                    .toList();
        }
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .password(user.getPassword())
                .roles(roleDtoList)
                .build();
    }
 */
}
