package project.apiservice.domain.exception;

public class PasswordDoNotMatch extends BaseException {
    public PasswordDoNotMatch() {
        super("FORBIDDEN");
    }
}
