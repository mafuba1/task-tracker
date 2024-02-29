package ru.nasrulaev.tasktrackerbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.oauth2.resourceserver.OAuth2ResourceServerSecurityMarker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nasrulaev.tasktrackerbackend.repository.UsersRepository;
import ru.nasrulaev.tasktrackerbackend.security.PersonDetails;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Autowired
    public PersonDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new PersonDetails(usersRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        ));
    }
}
