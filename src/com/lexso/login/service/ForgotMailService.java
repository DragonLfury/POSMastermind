package com.lexso.login.service;

import com.lexso.login.main.ForgotPassword;
import static com.lexso.login.main.ForgotPassword.LOGGER;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotMailService {

    public static void sendOtpEmail(String email, String firstName, String lastName, String otp) {
        try {
            String fromEmail = "geekhirusha@gmail.com";
            String emailPassword = "sltqhvxghjmpztlt";

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, emailPassword);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("LexSo POS - Password Reset OTP");

            String htmlContent = "<html>"
                    + "<body style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                    + "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;'>"
                    + "<h2 style='color: #2c3e50;'>Password Reset Request</h2>"
                    + "<p>Dear " + firstName + " " + lastName + ",</p>"
                    + "<p>We received a request to reset your password for LexSo POS system.</p>"
                    + "<div style='background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 15px 0; text-align: center;'>"
                    + "<p style='font-size: 24px; font-weight: bold; color: #3498db;'>Your OTP Code: " + otp + "</p>"
                    + "<p style='font-size: 12px; color: #7f8c8d;'>This OTP is valid for 5 minutes only.</p>"
                    + "</div>"
                    + "<p>If you didn't request this password reset, please ignore this email or contact support.</p>"
                    + "<p>Best regards,<br>LexSo POS Team</p>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            message.setContent(htmlContent, "text/html");
            Transport.send(message);

            LOGGER.info("OTP email sent to: " + email);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Error sending OTP email", e);
            // Don't show error to user to prevent email enumeration
        }
    }

    public static void sendPasswordResetConfirmation(String email) {
        try {
            String fromEmail = "geekhirusha@gmail.com";
            String emailPassword = "sltqhvxghjmpztlt";

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, emailPassword);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("LexSo POS - Password Reset Confirmation");
            message.setSentDate(new Date());

            String htmlContent = "<html>"
                    + "<body style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                    + "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;'>"
                    + "<h2 style='color: #2c3e50;'>Password Successfully Reset</h2>"
                    + "<p>This is a confirmation that your LexSo POS account password was successfully reset.</p>"
                    + "<div style='background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 15px 0;'>"
                    + "<p>If you did not perform this action, please contact our support team immediately.</p>"
                    + "</div>"
                    + "<p>For security reasons, we recommend that you:</p>"
                    + "<ul>"
                    + "<li>Keep your password confidential</li>"
                    + "<li>Change your password regularly</li>"
                    + "<li>Use a strong, unique password</li>"
                    + "</ul>"
                    + "<p>Best regards,<br>LexSo POS Team</p>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            message.setContent(htmlContent, "text/html");
            Transport.send(message);

            LOGGER.info("Password reset confirmation sent to: " + email);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Error sending password reset confirmation", e);
        }
    }

}
