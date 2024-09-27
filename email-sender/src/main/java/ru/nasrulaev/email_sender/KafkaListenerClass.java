package ru.nasrulaev.email_sender;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.nasrulaev.email_sender.model.Email;
import ru.nasrulaev.email_sender.model.RegistrationEmailContext;
import ru.nasrulaev.email_sender.service.EmailService;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Component
@KafkaListener(topics = "EMAIL_SENDING_TASKS", groupId = "EMAIL_SENDER")
public class KafkaListenerClass {

    private final EmailService emailService;

    @Autowired
    public KafkaListenerClass(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaHandler
    public void handleEmail(RegistrationEmailContext registrationEmail) throws MessagingException, UnsupportedEncodingException {
        Email email = new Email();
        Map<String, Object> context = Map.of("registrationToken", registrationEmail.getToken());

        email.setTo(registrationEmail.getTo());
        email.setSubject("Подтверждение регистрации");
        email.setContext(context);
        email.setTemplateLocation("registration");
        email.setFrom("noreply");
        emailService.sendEmail(email);
    }

}
