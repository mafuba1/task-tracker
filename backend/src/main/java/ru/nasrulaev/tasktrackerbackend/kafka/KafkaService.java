package ru.nasrulaev.tasktrackerbackend.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.nasrulaev.tasktrackerbackend.kafka.email.RegistrationEmailContext;

@Component
public class KafkaService {
    private final KafkaTemplate<String, RegistrationEmailContext> emailTemplate;

    @Autowired
    public KafkaService(KafkaTemplate<String, RegistrationEmailContext> emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public void sendMessage(RegistrationEmailContext email) {
        emailTemplate.send("EMAIL_SENDING_TASKS", email);
    }
}
