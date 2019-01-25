package com.example.demo;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
@Autowired
private Environment env;
@Autowired
private JavaMailSender javaMailSender;

public void sendMail(User user) throws MailException
{
	SimpleMailMessage mailMessage = new SimpleMailMessage();
	mailMessage.setTo(user.email);
	mailMessage.setFrom(env.getProperty("spring.mail.username"));
	mailMessage.setSubject("You have been granted "+user.role+"permission");
	mailMessage.setText("Hi "+user.firstName+" you are added as user and been granted "+user.role+"permission");
	javaMailSender.send(mailMessage);
}
}
