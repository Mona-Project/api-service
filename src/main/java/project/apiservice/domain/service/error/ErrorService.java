package project.apiservice.domain.service.error;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.apiservice.openapi.model.ErrorResponse;
import project.apiservice.shared.errors.ErrorProperties;

@RequiredArgsConstructor
@Service
public class ErrorService {
    private final ErrorProperties properties;

    public ErrorResponse getErrorResponse(String code) {
        return properties.getResponses()
                .getOrDefault(code,
                              new ErrorResponse()
                                      .status(500)
                                      .code("INTERNAL_SERVER_ERROR")
                                      .message("Unable To read the error")
                                      .suggestion("Address to the support team")
                );
    }
}
