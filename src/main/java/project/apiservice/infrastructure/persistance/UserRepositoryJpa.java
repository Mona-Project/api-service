package project.apiservice.infrastructure.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.apiservice.domain.model.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepositoryJpa extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findUserEntityById(UUID id);

    List<UserEntity> findByIdIn(List<UUID> ids);
}
