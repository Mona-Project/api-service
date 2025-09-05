package project.apiservice.infrastructure.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import project.apiservice.application.port.gateway.UserTeamsGateway;
import project.apiservice.openapi.model.TeamResponse;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UserTeamsGatewayImpl implements UserTeamsGateway {
    private final RestTemplate restTemplate;
    @Value("${team.service.url}")
    private String URL;


    @Override
    public void addUser(UUID teamId,
                        UUID userId) {
        final String baseUrl = URL + "/user-teams/{teamId}/user/{userId}";
        final String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .buildAndExpand(teamId,
                                userId
                )
                .toUriString();

        restTemplate.exchange(
                url,
                HttpMethod.POST,
                HttpEntity.EMPTY,
                Void.class,
                teamId,
                userId
        );
    }

    @Override
    public void deleteUser(UUID teamId,
                           UUID userId) {
        final String baseUrl = URL + "/user-teams/{teamId}/user/{userId}";
        final String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .buildAndExpand(teamId,
                                userId
                )
                .toUriString();

        restTemplate.exchange(url,
                              HttpMethod.DELETE,
                              HttpEntity.EMPTY,
                              Void.class,
                              teamId,
                              userId
        );
    }

    @Override
    public List<UUID> listTeamMembers(UUID teamId) {
        final String baseUrl = URL + "/user-teams/{teamID}/users";
        final String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .buildAndExpand(teamId)
                .toUriString();

        return restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<UUID>>() {
                        },
                        teamId
                )
                .getBody();
    }

    @Override
    public List<TeamResponse> listTeams(UUID userId) {
        final String baseUrl = URL + "/user-teams/{teamId}/teams";
        final String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .buildAndExpand(userId)
                .toUriString();

        return restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<TeamResponse>>() {
                        },
                        userId
                )
                .getBody();
    }
}