package project.apiservice.application.port.gateway;

import project.apiservice.openapi.model.TeamRequest;
import project.apiservice.openapi.model.TeamResponse;

import java.util.List;
import java.util.UUID;

public interface TeamGateway {
    TeamResponse createTeam(TeamRequest request);

    void deleteTeam(TeamRequest request);

    TeamResponse getTeamById(UUID id,
                             UUID userId);

    TeamResponse updateTeam(UUID id,
                            TeamRequest request,
                            UUID userId);

    TeamResponse deleteTeam(UUID id);

    List<UUID> listTeamMembers(UUID id);
}
