package ru.nasrulaev.email_sender;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.nasrulaev.email_sender.model.Email;
import ru.nasrulaev.email_sender.model.RegistrationEmailContext;
import ru.nasrulaev.email_sender.model.StatsEmailContext;
import ru.nasrulaev.email_sender.model.TaskInfo;
import ru.nasrulaev.email_sender.service.EmailService;

import java.io.UnsupportedEncodingException;
import java.util.List;
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

    @Value("${tasks_not_done_limit}")
    private int tasksNotDoneLimitForEmail;

    @KafkaHandler
    public void handleEmail(StatsEmailContext statsEmail) throws MessagingException, UnsupportedEncodingException {
        Email email = new Email();
        List<TaskInfo> tasksDoneToday = statsEmail.getTaskDoneToday();
        List<TaskInfo> tasksNotDone = statsEmail.getTasksNotDone();
        Map<String, Object> context = Map.of(
                "tasksDoneTodayCount", tasksDoneToday.size(),
                "taskDoneToday", tasksDoneToday,
                "tasksNotDoneCount", tasksNotDone.size(),
                "tasksNotDone", tasksNotDone.stream().limit(tasksNotDoneLimitForEmail).toList()
        );

        email.setTo(statsEmail.getTo());
        email.setSubject("Статистика за прошедщий день");
        email.setContext(context);
        email.setTemplateLocation("stats");
        email.setFrom("stats");
        emailService.sendEmail(email);
    }

}
