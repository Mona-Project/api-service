package project.apiservice.infrastructure.persistance;

import project.apiservice.domain.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepositoryJpa extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
}
