package project.apiservice.application.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.apiservice.application.port.UserManagementUseCase;
import project.apiservice.domain.enums.UserRole;
import project.apiservice.domain.model.UserEntity;
import project.apiservice.infrastructure.mapper.UserMapper;
import project.apiservice.infrastructure.security.jwt.JwtUtil;
import project.apiservice.openapi.model.UserResponse;
import project.apiservice.shared.utils.TokenExtractionUtils;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserManagementUseCase userService;
    private final JwtUtil jwtUtil;
    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<UserResponse> getUserInfo(HttpServletRequest request) {
        final String token = TokenExtractionUtils.extractToken(request);
        final Optional<UserEntity> retrievedUser = userService.findByUsername(jwtUtil.getUsernameFromJwtToken(token));

        final UserEntity user = retrievedUser.orElseGet(null);

        return ResponseEntity.ok(mapper.userEntityToUserResponse(user));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable UUID id) {
        final Optional<UserEntity> retrievedUser = userService.findUserById(id);

        final UserEntity user = retrievedUser.orElseGet(null);

        return ResponseEntity.ok(mapper.userEntityToUserResponse(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<UserResponse> updateUsersRole(@PathVariable UUID id,
                                                        @RequestBody UserRole role) {
        final Optional<UserEntity> userToUpdate = userService.findUserById(id);

        final UserEntity updatedUser = userToUpdate.orElseGet(null);
        updatedUser.setRole(role);

        final UserEntity user = userService.save(updatedUser);

        return ResponseEntity.ok(mapper.userEntityToUserResponse(user));
    }
}