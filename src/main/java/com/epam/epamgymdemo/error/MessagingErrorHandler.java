package com.epam.epamgymdemo.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.listener.adapter.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
@Slf4j
public class MessagingErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {
        if (t instanceof ListenerExecutionFailedException exception) {
            log.error("Listener failed to process the message. Message: {}, Exception: {}",
                    exception.getCause().getMessage(), t);
        } else {
            log.error("Unexpected error occurred: {}", t.getMessage(), t);
        }
    }
}
