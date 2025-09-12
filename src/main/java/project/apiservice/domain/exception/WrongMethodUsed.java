package project.apiservice.domain.exception;

public class WrongMethodUsed extends BaseException {
    public WrongMethodUsed() {
        super("METHOD_NOT_ALLOWED");
    }
}
