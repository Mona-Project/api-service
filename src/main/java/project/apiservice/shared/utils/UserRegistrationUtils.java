package project.apiservice.shared.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.BadCredentialsException;
import project.apiservice.domain.exception.InvalidCredentials;

@UtilityClass
public class UserRegistrationUtils {

    public void checkPasswordMatch(String p1,
                                   String p2) {
        if (!p1.equals(p2)) {
            throw new InvalidCredentials();
        }
    }
}
