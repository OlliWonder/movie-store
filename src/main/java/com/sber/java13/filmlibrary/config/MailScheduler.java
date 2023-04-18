package com.sber.java13.filmlibrary.config;

import com.sber.java13.filmlibrary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MailScheduler {
    private UserService userService;
    private JavaMailSender javaMailSender;
    
    @Autowired
    public void getUserService(UserService userService) {
        this.userService = userService;
    }
    
    @Autowired
    public void getEmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Scheduled(cron = "0 0 6 * * ?") //каждый день в 6 утра
    public void sendMailsToDebtors() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        List<String> emails = userService.getUserEmailsWithDelayedRentDate();
        if (emails.size() > 0) {
            mailMessage.setTo(emails.toArray(new String[0]));
            mailMessage.setSubject("Напоминание о просрочке книг(и)");
            mailMessage.setText("Добрый день. Сообщаем Вам, что заканчивается срок аренды фильма.");
            javaMailSender.send(mailMessage);
        }
        log.info("Планировщик работает!");
    }
}
