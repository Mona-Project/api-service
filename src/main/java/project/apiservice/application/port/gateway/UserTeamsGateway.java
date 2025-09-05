package project.apiservice.application.port.gateway;

import project.apiservice.openapi.model.TeamResponse;

import java.util.List;
import java.util.UUID;

public interface UserTeamsGateway {
    void addUser(UUID teamId,
                 UUID userId);

    void deleteUser(UUID teamId,
                    UUID userId);

    List<UUID> listTeamMembers(UUID teamId);

    List<TeamResponse> listTeams(UUID userId);
}
