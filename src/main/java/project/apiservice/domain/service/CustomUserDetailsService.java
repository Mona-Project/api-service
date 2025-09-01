package project.apiservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.apiservice.domain.model.UserEntity;
import project.apiservice.domain.model.security.CustomUserDetails;
import project.apiservice.infrastructure.persistance.UserRepositoryJpa;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepositoryJpa userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        final UserEntity user = optionalUser.get();

        return new CustomUserDetails(user.getUsername(),
                                     user.getPassword(),
                                     user.getRole()
        );
    }
}
