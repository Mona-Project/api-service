package project.apiservice.application.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import project.apiservice.domain.exception.BaseException;
import project.apiservice.domain.service.error.ErrorService;
import project.apiservice.openapi.model.ErrorResponse;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalHandler {
    private final ErrorService errorService;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> catchHandledExceptions(BaseException ex) {
        final ErrorResponse errorResponse = errorService.getErrorResponse(ex.getIdentifier());

        return ResponseEntity
                .status(errorResponse.getStatus())
//                .ok()
                  .body(errorResponse);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> catchDeniedException(AuthorizationDeniedException ex) {
        final ErrorResponse errorResponse = errorService.getErrorResponse("UNAUTHORIZED");

        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> catchException(Exception ex) throws Exception {
        System.err.println(ex);

        final ErrorResponse errorResponse = errorService.getErrorResponse("INTERNAL_SERVER_ERROR");
        errorResponse.setMessage("The issue cannot be identified");

        return ResponseEntity
                .status(Integer.parseInt(errorResponse.getCode()))
                .body(errorResponse);
    }
}
