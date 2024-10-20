package ru.nasrulaev.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.nasrulaev.scheduler.dto.StatsEmailContext;

@Component
public class KafkaService {
    private final KafkaTemplate<String, StatsEmailContext> emailTemplate;

    @Autowired
    public KafkaService(KafkaTemplate<String, StatsEmailContext> emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public void sendMessage(StatsEmailContext email) {
        emailTemplate.send("EMAIL_SENDING_TASKS", email);
    }
}
