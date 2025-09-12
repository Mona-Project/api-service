package project.apiservice.domain.exception;

public class InvalidBody extends BaseException {
    public InvalidBody() {
        super("BAD_REQUEST");
    }
}
