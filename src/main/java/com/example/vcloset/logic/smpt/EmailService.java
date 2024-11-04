package com.example.vcloset.logic.smpt;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender emailSender;

    private String htmlBody = "<html>" +
            "<head>" +
            "<style>" +
            "h1 { color: navy; }" +
            "p { font-size: 16px; }" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<h1>Hola!</h1>" +
            "<p>Este es un correo electrónico con <strong>estilos</strong>.</p>" +
            "</body>" +
            "</html>";

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("technest.smtp@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendHtmlMessage(String toAddress, String subject) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("technest.smtp@gmail.com");
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(this.htmlBody, true);
        emailSender.send(message);
    }
}
