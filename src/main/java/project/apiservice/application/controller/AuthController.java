package project.apiservice.application.controller;

import project.apiservice.openapi.model.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/apiservice")
@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody LoginRequest req) {
        final Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

        return auth.isAuthenticated() ? ResponseEntity.ok(true) : ResponseEntity.badRequest()
                .build();
    }
}
