package project.apiservice.application.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.apiservice.application.port.UserManagementUseCase;
import project.apiservice.application.port.gateway.TeamGateway;
import project.apiservice.domain.enums.UserRole;
import project.apiservice.domain.model.UserEntity;
import project.apiservice.infrastructure.mapper.UserMapper;
import project.apiservice.infrastructure.security.jwt.JwtUtil;
import project.apiservice.openapi.model.TeamRequest;
import project.apiservice.openapi.model.TeamResponse;
import project.apiservice.openapi.model.UserResponse;
import project.apiservice.shared.utils.TokenExtractionUtils;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/teams")
@RestController
public class TeamController {
    private final TeamGateway teamGateway;
    private final UserManagementUseCase userManagementService;
    private final UserMapper userMapper;
    private final JwtUtil util;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@RequestBody TeamRequest request) {
        final TeamResponse response = teamGateway.createTeam(request);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @DeleteMapping
    public void deleteTeam(@RequestBody TeamRequest request) {
        teamGateway.deleteTeam(request);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeamInfo(@PathVariable UUID teamId,
                                                    HttpServletRequest request) {
        final String token = TokenExtractionUtils.extractToken(request);
        final UserRole role = UserRole.valueOf(util.getRoleFromJwtToken(token));
        final UUID userID = util.getIdFromJwtToken(token);

        final TeamResponse response;

        if (role.equals(UserRole.ADMIN)) {
            response = teamGateway.getTeamById(teamId,
                                               null
            );
        } else {
            response = teamGateway.getTeamById(teamId,
                                               userID
            );
        }

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeamInfo(@PathVariable UUID id,
                                                       @RequestBody TeamRequest teamRequest,
                                                       HttpServletRequest request) {
        final String token = TokenExtractionUtils.extractToken(request);
        final UserRole role = UserRole.valueOf(util.getRoleFromJwtToken(token));
        final UUID userID = util.getIdFromJwtToken(token);

        final TeamResponse response;

        if (role.equals(UserRole.ADMIN)) {
            response = teamGateway.updateTeam(id,
                                              teamRequest,
                                              null
            );
        } else {
            response = teamGateway.updateTeam(id,
                                              teamRequest,
                                              userID
            );
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserResponse>> getTeamUsers(@PathVariable UUID id) {
        final List<UUID> listOfUUID = teamGateway.listTeamMembers(id);
        final List<UserEntity> list = userManagementService.findUsersById(listOfUUID);

        final List<UserResponse> response = userMapper.userEntityListToResponseList(list);

        return ResponseEntity.ok(response);
    }
}
