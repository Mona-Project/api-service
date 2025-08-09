package project.apiservice.application.port;

import project.apiservice.domain.model.UserEntity;

import java.util.Optional;

public interface UserManagementUseCase {
    UserEntity save(UserEntity user);

    Optional<UserEntity> findByUsername(String username);
}
