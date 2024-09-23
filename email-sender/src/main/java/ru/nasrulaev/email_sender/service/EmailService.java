package ru.nasrulaev.email_sender.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import ru.nasrulaev.email_sender.model.Email;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public EmailService(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") JavaMailSender emailSender, SpringTemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(Email email) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(email.getContext());
        String emailContent = templateEngine.process(
                "email/" + email.getTemplateLocation(),
                context
        );

        mimeMessageHelper.setTo(email.getTo());
        mimeMessageHelper.setSubject(email.getSubject());
        mimeMessageHelper.setFrom(email.getFrom());
        mimeMessageHelper.setText(emailContent, true);
        emailSender.send(message);
    }
}
