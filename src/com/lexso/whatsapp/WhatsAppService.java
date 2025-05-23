package com.lexso.whatsapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WhatsAppService {

    private static final String API_URL = "https://api.geekhirusha.com/whatsapp.php";
    
    public enum MediaType {
        IMAGE("image"),
        VIDEO("video"),
        AUDIO("audio"),
        PDF("pdf"),
        TEXT("text");
        
        private final String value;
        
        MediaType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    public void sendMessage(String phoneNumber, String message) throws Exception {
        sendMediaMessage(phoneNumber, message, MediaType.TEXT, null); 
    }
    
    /**
     * Sends a message with media attachment or a plain text message
     * by including the 'type' parameter as required by the API.
     * * @param phoneNumber The recipient's phone number
     * @param message The text message to send
     * @param mediaType The type of media (image, video, audio, pdf, text)
     * @param mediaUrl URL to the media file (can be null for MediaType.TEXT)
     * @throws Exception If the message fails to send or if media type is invalid
     */
    public void sendMediaMessage(String phoneNumber, String message, MediaType mediaType, String mediaUrl) throws Exception {
        if (mediaType != MediaType.TEXT && (mediaUrl == null || mediaUrl.trim().isEmpty())) {
            throw new IllegalArgumentException("Media URL cannot be empty for media messages of type " + mediaType.name());
        }
        
        if (mediaType != MediaType.TEXT) {
            validateMediaUrl(mediaUrl, mediaType);
        }
        
        String encodedMessage = URLEncoder.encode(message, "UTF-8");
        String encodedMediaUrl = (mediaUrl != null) ? URLEncoder.encode(mediaUrl, "UTF-8") : "";
        
        String fullUrl = API_URL + "?number=" + phoneNumber + "&type=" + mediaType.getValue();
        
        if (message != null && !message.trim().isEmpty()) {
            fullUrl += "&message=" + encodedMessage;
        }
        
        if (mediaType != MediaType.TEXT && !encodedMediaUrl.isEmpty()) {
            fullUrl += "&mediaUrl=" + encodedMediaUrl;
        }

        URL url = new URL(fullUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            BufferedReader errorIn = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String errorLine;
            while ((errorLine = errorIn.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorIn.close();
            
            throw new Exception("Failed to send WhatsApp message. Response Code: " + responseCode + ". API Response: " + errorResponse.toString());
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("WhatsApp Response: " + response.toString());
    }
    
    public void sendImageMessage(String phoneNumber, String message, String imageUrl) throws Exception {
        sendMediaMessage(phoneNumber, message, MediaType.IMAGE, imageUrl);
    }
    
    public void sendVideoMessage(String phoneNumber, String message, String videoUrl) throws Exception {
        sendMediaMessage(phoneNumber, message, MediaType.VIDEO, videoUrl);
    }
    
    public void sendAudioMessage(String phoneNumber, String message, String audioUrl) throws Exception {
        sendMediaMessage(phoneNumber, message, MediaType.AUDIO, audioUrl);
    }
    
    public void sendPdfMessage(String phoneNumber, String message, String pdfUrl) throws Exception {
        sendMediaMessage(phoneNumber, message, MediaType.PDF, pdfUrl);
    }
    
    private void validateMediaUrl(String mediaUrl, MediaType mediaType) throws IllegalArgumentException {
        mediaUrl = mediaUrl.toLowerCase();
        
        switch (mediaType) {
            case IMAGE:
                if (!mediaUrl.matches(".*\\.(jpg|jpeg|png|gif|bmp|webp)($|\\?.*)")) {
                    throw new IllegalArgumentException("Invalid image URL. URL must point to a jpg, jpeg, png, gif, bmp, or webp file.");
                }
                break;
            case VIDEO:
                if (!mediaUrl.matches(".*\\.(mp4|mov|avi|wmv|flv|webm|mkv)($|\\?.*)")) {
                    throw new IllegalArgumentException("Invalid video URL. URL must point to an mp4, mov, avi, wmv, flv, webm, or mkv file.");
                }
                break;
            case AUDIO:
                if (!mediaUrl.matches(".*\\.(mp3|wav|ogg|m4a|aac|flac)($|\\?.*)")) {
                    throw new IllegalArgumentException("Invalid audio URL. URL must point to an mp3, wav, ogg, m4a, aac, or flac file.");
                }
                break;
            case PDF:
                if (!mediaUrl.matches(".*\\.pdf($|\\?.*)")) {
                    throw new IllegalArgumentException("Invalid PDF URL. URL must point to a PDF file.");
                }
                break;
            case TEXT: 
                break;
            default:
                break;
        }
    }
}