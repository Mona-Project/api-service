package project.apiservice.infrastructure.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import project.apiservice.application.port.gateway.TaskGateway;
import project.apiservice.openapi.model.TaskRequest;
import project.apiservice.openapi.model.TaskResponse;
import project.apiservice.openapi.model.UpdateTaskAssignee;
import project.apiservice.openapi.model.UpdateTaskStatus;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TaskGatewayImpl implements TaskGateway {
    private final RestTemplate restTemplate;
    @Value("${task.service.url}")
    private String URL;

    @Override
    public TaskResponse createTask(TaskRequest request) {
        final String url = URL + "/tasks";

        return restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        new HttpEntity<>(request),
                        TaskResponse.class
                )
                .getBody();
    }

    @Override
    public List<TaskResponse> getUserTasks(UUID userId) {
        final String url = URL + "/tasks/assigned/{userId}";

        return restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<TaskResponse>>() {
                        },
                        userId
                )
                .getBody();
    }

    @Override
    public List<TaskResponse> getTeamTasks(UUID teamId) {
        final String url = URL + "/tasks/team/{teamId}";

        return restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<List<TaskResponse>>() {
                        },
                        teamId
                )
                .getBody();
    }

    @Override
    public TaskResponse updateTask(UUID taskId,
                                   UpdateTaskStatus request,
                                   UUID userId) {
        final String baseUrl = URL + "/tasks/{taskId}/status";
        final String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .queryParam("userId",
                            userId
                )
                .buildAndExpand(taskId)
                .toUriString();

        return restTemplate.exchange(
                        url,
                        HttpMethod.PUT,
                        new HttpEntity<>(request),
                        TaskResponse.class,
                        taskId
                )
                .getBody();
    }

    @Override
    public TaskResponse updateTaskAssign(UUID taskId,
                                         UpdateTaskAssignee updateRequest) {
        final String url = URL + "/tasks/{taskId}/assign";

        return restTemplate.exchange(
                        url,
                        HttpMethod.PUT,
                        new HttpEntity<>(updateRequest),
                        TaskResponse.class,
                        taskId
                )
                .getBody();
    }
}
