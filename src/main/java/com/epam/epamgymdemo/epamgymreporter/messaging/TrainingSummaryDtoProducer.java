package com.epam.epamgymdemo.epamgymreporter.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingSummaryDtoProducer {

    private static final String TRAINING_SUMMARY_QUEUE = "training.summary.queue";

    private final JmsTemplate jmsTemplate;

    public void send(String username) {
        jmsTemplate.convertAndSend(TRAINING_SUMMARY_QUEUE, username);
    }
}
