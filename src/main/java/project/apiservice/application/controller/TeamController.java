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
import project.apiservice.application.port.gateway.TeamGateway;
import project.apiservice.domain.enums.UserRole;
import project.apiservice.domain.model.UserEntity;
import project.apiservice.infrastructure.security.jwt.JwtUtil;
import project.apiservice.openapi.model.TeamRequest;
import project.apiservice.openapi.model.TeamResponse;
import project.apiservice.shared.TokenExtractionUtils;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/teams")
@RestController
public class TeamController {
    private final TeamGateway teamGateway;
    private final JwtUtil util;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@RequestBody TeamRequest request) {
        final TeamResponse response = teamGateway.createTeam(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getTeamInfo(@PathVariable UUID id,
                                                    HttpServletRequest request) {
        final String token = TokenExtractionUtils.extractToken(request);
        final UserRole role = UserRole.valueOf(util.getRoleFromJwtToken(token));
        final UUID userID = util.getIdFromJwtToken(token);

        final TeamResponse response;

        if (role.equals(UserRole.ADMIN)) {
            response = teamGateway.getTeamById(id, null);
        } else {
            response = teamGateway.getTeamById(id, userID);
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
            response = teamGateway.updateTeam(id, teamRequest, null);
        } else {
            response = teamGateway.updateTeam(id, teamRequest, userID);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<UserEntity>> getTeamUsers(@PathVariable UUID id) {
        final List<UserEntity> list = teamGateway.listTeamMembers(id);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}/managers")
    public ResponseEntity<List<UserEntity>> getTeamManagers(@PathVariable UUID id) {
        final List<UserEntity> list = teamGateway.listTeamManagers(id);

        return ResponseEntity.ok(list);
    }
}
