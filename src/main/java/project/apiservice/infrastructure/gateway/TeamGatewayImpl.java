package project.apiservice.infrastructure.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import project.apiservice.application.port.gateway.TeamGateway;
import project.apiservice.openapi.model.TeamRequest;
import project.apiservice.openapi.model.TeamResponse;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TeamGatewayImpl implements TeamGateway {
    private final RestTemplate restTemplate;
    @Value("${team.service.url}")
    private String URL;

    @Override
    public TeamResponse createTeam(TeamRequest request) {
        final String url = URL + "/teams";
        return restTemplate.postForObject(url,
                                          request,
                                          TeamResponse.class
        );
    }

    @Override
    public void deleteTeam(TeamRequest request) {
        final String url = URL + "/teams";

        restTemplate.exchange(url,
                              HttpMethod.DELETE,
                              new HttpEntity<>(request),
                              Void.class
        );
    }

    @Override
    public TeamResponse getTeamById(UUID id,
                                    UUID userId) {
        final String baseUrl = URL + "/teams/{teamId}";
        final String url;

        if (userId != null) {
            url = UriComponentsBuilder.fromUriString(baseUrl)
                    .queryParam("userId",
                                userId
                    )
                    .toUriString();
        } else {
            url = baseUrl;
        }

        return restTemplate.getForObject(url,
                                         TeamResponse.class,
                                         id
        );
    }

    @Override
    public TeamResponse updateTeam(UUID id,
                                   TeamRequest request,
                                   UUID userId) {
        final String baseUrl = URL + "/teams/{id}";
        final String url;

        if (userId != null) {
            url = UriComponentsBuilder.fromUriString(baseUrl)
                    .queryParam("userId",
                                userId
                    )
                    .toUriString();
        } else {
            url = baseUrl;
        }
        return restTemplate.exchange(url,
                                     HttpMethod.PUT,
                                     new HttpEntity<>(request),
                                     TeamResponse.class,
                                     id
                )
                .getBody();
    }

    @Override
    public TeamResponse deleteTeam(UUID id) {
        final String url = URL + "/teams/{id}";

        return restTemplate.exchange(url,
                                     HttpMethod.DELETE,
                                     HttpEntity.EMPTY,
                                     TeamResponse.class,
                                     id
                )
                .getBody();
    }

    @Override
    public List<UUID> listTeamMembers(UUID id) {
        final String url = URL + "/teams/{id}/users";

        return restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<UUID>>() {
                        },
                        id
                )
                .getBody();
    }
}
