package project.apiservice.domain.exception;

public class InvalidCredentials extends BaseException {
    public InvalidCredentials() {
        super("FORBIDDEN");
    }
}
