package project.apiservice.domain.exception;

public class NoAccess extends BaseException {
    public NoAccess() {
        super("UNAUTHORIZED");
    }
}
