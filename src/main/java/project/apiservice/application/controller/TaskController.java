package project.apiservice.application.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.apiservice.application.port.gateway.TaskGateway;
import project.apiservice.infrastructure.security.jwt.JwtUtil;
import project.apiservice.openapi.model.TaskRequest;
import project.apiservice.openapi.model.TaskResponse;
import project.apiservice.openapi.model.UpdateTaskAssignee;
import project.apiservice.openapi.model.UpdateTaskStatus;
import project.apiservice.shared.utils.TokenExtractionUtils;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskGateway taskGateway;
    private final JwtUtil util;

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        final TaskResponse response = taskGateway.createTask(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/assigned")
    public ResponseEntity<List<TaskResponse>> getUserTasks(HttpServletRequest request) {
        final UUID userId = extractUserId(request);

        final List<TaskResponse> response = taskGateway.getUserTasks(userId);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<TaskResponse>> getTeamTasks(@PathVariable UUID teamId) {
        final List<TaskResponse> response = taskGateway.getTeamTasks(teamId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable UUID taskId,
                                                   @RequestBody UpdateTaskStatus updateRequest,
                                                   HttpServletRequest request) {
        final UUID userId = extractUserId(request);

        final TaskResponse response = taskGateway.updateTask(taskId,
                                                             updateRequest,
                                                             userId
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/{taskId}/assign")
    public ResponseEntity<TaskResponse> updateTaskAssign(@PathVariable UUID taskId,
                                                         @RequestBody UpdateTaskAssignee updateRequest) {
        final TaskResponse response = taskGateway.updateTaskAssign(taskId,
                                                                   updateRequest
        );

        return ResponseEntity.ok(response);
    }

    private UUID extractUserId(HttpServletRequest request) {
        final String token = TokenExtractionUtils.extractToken(request);

        return util.getIdFromJwtToken(token);
    }
}
