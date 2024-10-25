package com.elvo.service.impl;

import com.elvo.controller.ui.UIManager;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class SendBill {

    public static void sendBill(String to, String subject, String msg, String filePath) {
        // Configuration
        String host = "smtp.gmail.com";
        final String user = "kalpaselvitigala@gmail.com";
        final String password = "wsos hstk lumd lzyg"; // Consider using environment variables or a secure way to store credentials

        // Server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");

        // Session Object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(msg);

            // Create a multipart message
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Attach the PDF file
            if (filePath != null) {
                MimeBodyPart attachPart = new MimeBodyPart();
                attachPart.attachFile(new File(filePath)); // Attach the file
                multipart.addBodyPart(attachPart);
            }

            // Set the complete message parts
            message.setContent(multipart);

            // Send the email
            Transport.send(message);
            System.out.println("Sent successfully");

        } catch (AddressException e) {
            UIManager.showDialogueBox("Something Went Wrong", "Please re-check your email. If it is correct, please try again later.");
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            UIManager.showDialogueBox("Something Went Wrong", "Please re-check your email");
            throw new RuntimeException(e);
        } catch (Exception e) {
            UIManager.showDialogueBox("Attachment Error", "Failed to attach the file. Please try again.");
            throw new RuntimeException(e);
        }
    }
}
