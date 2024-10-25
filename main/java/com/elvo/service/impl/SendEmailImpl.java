package com.elvo.service.impl;

import com.elvo.controller.ui.UIManager;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class SendEmailImpl {

    public static void sendMail(String to, String subject, String msg){
        //Configuration
        String host = "smtp.gmail.com";
        final String user = "kalpaselvitigala@gmail.com";
        final String password = "rbuo nofe mdtk tkmh";

        //Server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host",host);
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.port","587");

        //Session Object
        Session session = Session.getInstance(properties, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            //default mime obj
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(msg);

            //send
            Transport.send(message);
            System.out.println("Sent successfully");

        } catch (AddressException e) {
            UIManager.showDialogueBox("Something Went Wrong","Please re-check your email. If it correct, please try again later");
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            UIManager.showDialogueBox("Something Went Wrong","Please re-check your email");
            throw new RuntimeException(e);
        }
    }
}
