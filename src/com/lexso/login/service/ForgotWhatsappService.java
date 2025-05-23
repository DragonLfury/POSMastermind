package com.lexso.login.service;

import static com.lexso.login.main.ForgotPassword.LOGGER;
import com.lexso.whatsapp.WhatsAppService;
import java.util.logging.Level;

public class ForgotWhatsappService {
    
    public static void sendOtpWhatsApp(String mobileNumber, String firstName, String lastName, String otp) {
        try {
            String formattedNumber = mobileNumber.startsWith("0") ? "94" + mobileNumber.substring(1) : mobileNumber;

            String message = "🔐 *LexSo POS Password Reset* 🔐\n\n"
                    + "Hello " + firstName + " " + lastName + ",\n\n"
                    + "You requested a password reset for your LexSo POS account.\n\n"
                    + "📋 *Your OTP Code:* `" + otp + "`\n"
                    + "⏳ *Valid for:* 5 minutes\n\n"
                    + "⚠️ *Do not share this code with anyone!*\n\n"
                    + "If you didn't request this, please contact support immediately.\n\n"
                    + "Best regards,\n"
                    + "LexSo POS Team";

            WhatsAppService whatsappService = new WhatsAppService();
            whatsappService.sendMediaMessage(formattedNumber, message, WhatsAppService.MediaType.TEXT, null);

            LOGGER.info("OTP WhatsApp message sent to: " + formattedNumber);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error sending OTP WhatsApp", e);
            // Don't show error to user to prevent phone number enumeration
        }
    }

}
