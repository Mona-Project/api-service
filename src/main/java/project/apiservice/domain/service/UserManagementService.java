package project.apiservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.apiservice.application.port.UserManagementUseCase;
import project.apiservice.domain.model.UserEntity;
import project.apiservice.infrastructure.persistance.UserRepositoryJpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserManagementService implements UserManagementUseCase {
    private final PasswordEncoder encoder;
    private final UserRepositoryJpa repositoryJpa;

    @Override
    public UserEntity save(UserEntity user) {
        user.setPassword(encoder.encode(user.getPassword()));

        return repositoryJpa.save(user);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return repositoryJpa.findByUsername(username);
    }

    @Override
    public Optional<UserEntity> findUserById(UUID id) {
        return repositoryJpa.findUserEntityById(id);
    }

    @Override
    public List<UserEntity> findUsersById(List<UUID> list) {
        return repositoryJpa.findByIdIn(list);
    }
}
