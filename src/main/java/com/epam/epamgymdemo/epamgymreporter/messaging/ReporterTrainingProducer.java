package com.epam.epamgymdemo.epamgymreporter.messaging;

import com.epam.epamgymdemo.model.dto.ReporterTrainingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReporterTrainingProducer {

    private static final String DB_OPERATIONS_QUEUE = "db.operations.queue";

    private static final String REPORTER_DTO_NAME = "com.epam.epamgymreporter.model.dto.TrainingDto";

    private final JmsTemplate jmsTemplate;

    public void send(ReporterTrainingDto reporterTrainingDto) {
        jmsTemplate.convertAndSend(DB_OPERATIONS_QUEUE, reporterTrainingDto, message -> {
            message.setStringProperty("_type", REPORTER_DTO_NAME);
            return message;
        });
    }
}
