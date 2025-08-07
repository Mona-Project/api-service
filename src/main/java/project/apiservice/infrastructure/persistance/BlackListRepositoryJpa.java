package project.apiservice.infrastructure.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.apiservice.domain.model.BlackListEntity;

import java.util.UUID;

@Repository
public interface BlackListRepositoryJpa extends JpaRepository<BlackListEntity, UUID> {
    boolean existsByToken(String token);
}
