package ru.nasrulaev.email_sender;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.nasrulaev.email_sender.model.Email;
import ru.nasrulaev.email_sender.model.RegistrationEmailContext;
import ru.nasrulaev.email_sender.service.EmailService;

import java.util.Collections;

@Component
@KafkaListener(topics = "EMAIL_SENDING_TASKS", groupId = "EMAIL_SENDER")
public class KafkaListenerClass {

    private final EmailService emailService;

    @Autowired
    public KafkaListenerClass(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaHandler
    public void handleEmail(RegistrationEmailContext registrationEmail) throws MessagingException {
        Email email = new Email();
        email.setTo(registrationEmail.getTo());
        email.setContext(Collections.emptyMap());
        email.setSubject("Welcome!");
        email.setTemplateLocation("registration");
        email.setFrom("noreply@mafuba.ru");
        emailService.sendEmail(email);
    }

}
