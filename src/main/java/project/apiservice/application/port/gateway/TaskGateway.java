package project.apiservice.application.port.gateway;

import project.apiservice.openapi.model.TaskRequest;
import project.apiservice.openapi.model.TaskResponse;
import project.apiservice.openapi.model.UpdateTaskAssignee;
import project.apiservice.openapi.model.UpdateTaskStatus;

import java.util.List;
import java.util.UUID;

public interface TaskGateway {
    TaskResponse createTask(TaskRequest request);

    List<TaskResponse> getUserTasks(UUID userId);

    List<TaskResponse> getTeamTasks(UUID teamId);

    TaskResponse updateTask(UUID taskId,
                            UpdateTaskStatus request,
                            UUID userId);

    TaskResponse updateTaskAssign(UUID taskId,
                                  UpdateTaskAssignee updateRequest);
}
