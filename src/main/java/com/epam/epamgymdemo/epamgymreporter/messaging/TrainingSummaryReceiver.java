package com.epam.epamgymdemo.epamgymreporter.messaging;

import com.epam.epamgymdemo.model.dto.TrainingSummaryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TrainingSummaryReceiver {

    private static final String TRAINING_SUMMARY_QUEUE = "training.summary.queue";

    private final JmsTemplate jmsTemplate;

    private final ObjectMapper objectMapper;

    public TrainingSummaryDto sendAndReceive(String username) {
        Message response = jmsTemplate.sendAndReceive(TRAINING_SUMMARY_QUEUE, session -> {
            Message message = session.createTextMessage(username);
            message.setJMSReplyTo(session.createTemporaryQueue());

            return message;
        });

        if(response != null) {
            try {
                return objectMapper.readValue(((TextMessage) response).getText(), TrainingSummaryDto.class);
            } catch (JMSException | IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else throw new RuntimeException("Something went wrong, please try again");
    }
}
