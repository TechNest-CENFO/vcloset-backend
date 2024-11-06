package com.example.vcloset.logic.service.smpt;

import jakarta.mail.MessagingException;

public interface IEmailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendHtmlMessage(String to, String subject) throws MessagingException;
}