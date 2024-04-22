package ru.itgirl.libraryproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itgirl.libraryproject.model.entity.User;
import ru.itgirl.libraryproject.repository.UserRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByLogin(login);
        if (user.isPresent()) {
            UserDetails userDetails = (UserDetails) UserService.builder()
                    .login(user.get().getLogin())
                    .password(user.get().getPassword())
                    .authorities(user.get().getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList()));
            return userDetails;
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
}
