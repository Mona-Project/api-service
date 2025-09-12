package project.apiservice.shared.errors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import project.apiservice.openapi.model.ErrorResponse;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "errors")
@PropertySource(value = "classpath:errors.yml", factory = YamlPropertySourceFactory.class)
public class ErrorProperties {
    private Map<String, ErrorResponse> responses;
}
