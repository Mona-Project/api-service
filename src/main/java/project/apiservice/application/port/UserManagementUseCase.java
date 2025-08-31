package project.apiservice.application.port;

import project.apiservice.domain.model.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserManagementUseCase {
    UserEntity save(UserEntity user);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findUserById(UUID id);

    List<UserEntity> findUsersById(List<UUID> list);
}
