package project.apiservice.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.apiservice.application.port.UserManagementUseCase;
import project.apiservice.application.port.gateway.UserTeamsGateway;
import project.apiservice.domain.model.UserEntity;
import project.apiservice.infrastructure.mapper.UserMapper;
import project.apiservice.openapi.model.TeamResponse;
import project.apiservice.openapi.model.UserResponse;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user-teams")
public class UserTeamController {
    private final UserTeamsGateway userTeamsGateway;
    private final UserManagementUseCase userManagementService;
    private final UserMapper userMapper;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PostMapping("/{teamId}/user/{userId}")
    public void addUser(@PathVariable UUID teamId,
                        @PathVariable UUID userId) {
        userTeamsGateway.addUser(
                teamId,
                userId
        );
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @DeleteMapping("/{teamId}/user/{userId}")
    public void deleteUser(@PathVariable UUID teamId,
                           @PathVariable UUID userId) {
        userTeamsGateway.deleteUser(teamId,
                                    userId
        );
    }

    @GetMapping("/{teamId}/users")
    public ResponseEntity<List<UserResponse>> listTeamMembers(@PathVariable UUID teamId) {
        final List<UUID> listOfMembers = userTeamsGateway.listTeamMembers(teamId);
        final List<UserEntity> listOfEntities = userManagementService.findUsersById(listOfMembers);

        final List<UserResponse> response = userMapper.userEntityListToResponseList(listOfEntities);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/teams")
    public ResponseEntity<List<TeamResponse>> listTeams(@PathVariable UUID userId) {
        return ResponseEntity.ok(userTeamsGateway.listTeams(userId));
    }
}