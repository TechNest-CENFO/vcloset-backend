package com.example.vcloset.logic.smpt;

import com.example.vcloset.logic.entity.auth.JwtService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private JwtService jwtService;


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
        String uniqueToken = UUID.randomUUID().toString();
        String htmlBody = String.format("""
        <div style="width: 100%%; display: flex; justify-content: center; align-items: center; background-color: #f3f4f6; padding: 20px;">
            <div style="width: 400px; background-color: white; padding: 20px; text-align: center;">
                <div style="margin-bottom: 20px;">
                    <img src="https://res.cloudinary.com/dklipon9i/image/upload/v1730752257/Screenshot_from_2024-10-10_11-07-25-transformed_im7dcc.png" alt="Logo" style="width: 150px;">
                </div>
                <h3 style="color: #FF4C3C; margin-bottom: 20px;">Restablece tu Contraseña</h3>
                <div style="margin-bottom: 20px;">
                    <a href="http://localhost:4200/password-reset/%s" style="display: inline-block; padding: 10px 20px; color: white; background-color: #FF4C3C; text-decoration: none; border-radius: 10px;">
                        Restablecer Contraseña
                    </a>
                </div>
                <p style="color: gray; font-size: small; font-style: italic; margin: 0;">
                    Powered By &copy; TechNest
                </p>
            </div>
        </div>
        """, uniqueToken);


        jwtService.generatePasswordResetToken(toAddress, uniqueToken);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("technest.smtp@gmail.com");
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        emailSender.send(message);

    }
}
