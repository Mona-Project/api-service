package project.apiservice.shared.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TokenExtractionUtils {
    public String extractToken(HttpServletRequest request) {
        String token = "";

        final String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }
        return token;
    }
}
