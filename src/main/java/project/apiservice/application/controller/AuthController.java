package project.apiservice.application.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.apiservice.application.port.UserManagementUseCase;
import project.apiservice.domain.exception.InvalidCredentials;
import project.apiservice.domain.model.UserEntity;
import project.apiservice.infrastructure.mapper.UserMapper;
import project.apiservice.infrastructure.security.jwt.JwtUtil;
import project.apiservice.openapi.model.LoginRequest;
import project.apiservice.openapi.model.LoginResponse;
import project.apiservice.openapi.model.UserRegistrationRequest;
import project.apiservice.openapi.model.UserResponse;
import project.apiservice.shared.utils.UserRegistrationUtils;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final UserMapper mapper;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserManagementUseCase userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        try {

            final Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(),
                                                                                                                   req.getPassword()
            ));
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentials();
        }

        final String username = req.getUsername();

        final Optional<UserEntity> optionalUser = userService.findByUsername(username);
        final UserEntity user = optionalUser.orElse(null);

        final String token = jwtUtil.generateJwtToken(user.getId(),
                                                      username,
                                                      user.getRole()
        );

        return ResponseEntity.ok(new LoginResponse(user.getId()
                                                           .toString(),
                                                   username,
                                                   user.getRole()
                                                           .toString(),
                                                   token
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        jwtUtil.invalidateToken(request);

        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody
                                                 UserRegistrationRequest request) {
        UserRegistrationUtils.checkPasswordMatch(request.getPassword(),
                                                 request.getRepeatedPassword()
        );

        final UserEntity entity = mapper.userRequestToUserEntity(request);
        final UserEntity savedUser = userService.save(entity);

        return ResponseEntity.ok(mapper.userEntityToUserResponse(savedUser));
    }
}
