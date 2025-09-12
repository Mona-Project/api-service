package project.apiservice.domain.exception;

public class ServerError extends BaseException {
    public ServerError() {
        super("INTERNAL_SERVER_ERROR");
    }
}
