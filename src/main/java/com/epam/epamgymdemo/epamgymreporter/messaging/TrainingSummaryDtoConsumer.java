    package com.epam.epamgymdemo.epamgymreporter.messaging;

    import com.epam.epamgymdemo.model.dto.TrainingSummaryDto;
    import com.epam.epamgymdemo.service.TrainerService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.jms.annotation.JmsListener;
    import org.springframework.stereotype.Component;

    @Component
    @RequiredArgsConstructor
    public class TrainingSummaryDtoConsumer {

        private final TrainerService trainerService;

        @JmsListener(destination = "training.summary.response.queue")
        public void receive(TrainingSummaryDto trainingSummaryDto) {
            trainerService.processMonthlySummary(trainingSummaryDto);
        }
    }
