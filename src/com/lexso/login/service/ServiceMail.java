package com.lexso.login.service;

import com.lexso.login.model.ModelMessage;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ServiceMail {

    public ModelMessage sendMain(String toEmail, String code) {
        ModelMessage ms = new ModelMessage(false, "");
        String from = "geekhirusha@gmail.com";
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        String username = "geekhirusha@gmail.com";
        String password = "sltqhvxghjmpztlt";
        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Your OTP Code");

            String htmlContent = "<!DOCTYPE html>"
                    + "<html lang='en'>"
                    + "<head>"
                    + "<meta charset='UTF-8'>"
                    + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<style>"
                    + "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #e9ecef; margin: 0; padding: 20px; }"
                    + ".container { max-width: 500px; margin: auto; background: white; border-radius: 10px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2); padding: 30px; }"
                    + "h1 { color: #495057; text-align: center; }"
                    + "p { font-size: 18px; line-height: 1.6; color: #495057; }"
                    + ".otp { display: inline-block; font-size: 24px; font-weight: bold; color: #4CAF50; background: #e0f7fa; padding: 10px 20px; border-radius: 5px; margin: 20px 0; }"
                    + ".footer { text-align: center; margin-top: 30px; font-size: 14px; color: #868e96; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='container'>"
                    + "<h1>OTP Verification</h1>"
                    + "<p>Your OTP code is:</p>"
                    + "<center><div class='otp'>" + code + "</div></center>"
                    + "<p>Please enter this code to complete your verification process.</p>"
                    + "<div class='footer'>"
                    + "<p>Thank you for trusting us!</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            message.setContent(htmlContent, "text/html");

            Transport.send(message);

            ms.setSuccess(true);
            ms.setMessage("OTP sent successfully to " + toEmail);
        } catch (MessagingException e) {
            // Handle messaging exceptions
            if (e.getMessage().contains("Invalid Addresses")) {
                ms.setMessage("Invalid email address. Please check and try again.");
            } else {
                ms.setMessage("An error occurred while sending the OTP. Please try again later.");
            }
        }
        return ms;
    }
}
