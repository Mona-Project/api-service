package project.apiservice.application.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(Exception.class)
    public void catchException(Exception ex) throws Exception {

        System.err.println(ex);

        throw new Exception();
    }
}
