package project.apiservice.shared;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.BadCredentialsException;

@UtilityClass
public class UserRegistrationUtils {

    public void checkPasswordMatch(String p1,
                                   String p2) {
        if (!p1.equals(p2)) {
            throw new BadCredentialsException("Password do not match");
        }
    }
}
