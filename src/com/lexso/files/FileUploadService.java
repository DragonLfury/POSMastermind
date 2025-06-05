package com.lexso.files;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileUploadService {

    private static final String API_URL = "https://api.geekhirusha.com/fileUpload.php";

    /**
     * Uploads a file to the server using HTTP POST with multipart/form-data.
     *
     * @param filePath The path to the file to upload.
     * @return The server's response, which now includes the uploaded file path if successful.
     * @throws Exception If an error occurs during the upload process.
     */
    public String uploadFile(String filePath) throws Exception {
        // Create file object
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        String boundary = "---" + System.currentTimeMillis() + "---";
        String crlf = "\r\n";

        HttpURLConnection connection = null;
        OutputStream outputStream = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            outputStream = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

            
            writer.append("--" + boundary).append(crlf);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"").append(crlf);
            writer.append("Content-Type: " + Files.probeContentType(Paths.get(filePath))).append(crlf); // Detect content type
            writer.append(crlf).flush();
            Files.copy(Paths.get(filePath), outputStream); //send file bytes
            outputStream.flush();
            writer.append(crlf).flush();

            writer.append("--" + boundary + "--").append(crlf).flush();
            writer.close();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("Failed to upload file. Response Code: " + responseCode);
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            String serverResponse = response.toString();
            if (serverResponse.trim().equals("File uploaded successfully!")) {
                response.setLength(0); // Clear the StringBuilder
                response.append("https://api.geekhirusha.com/uploads/").append(file.getName()); //construct the file URL.
            }
            return response.toString();
        } finally {
            
            if (outputStream != null) try { outputStream.close(); } catch (IOException ignored) {}
            if (reader != null) try { reader.close(); } catch (IOException ignored) {}
            if (connection != null) connection.disconnect();
        }
    }

//    public static void main(String[] args) {
//        FileUploadService service = new FileUploadService();
//        try {
//            // Replace with the actual path to your file.
//            String filePath = "path/to/your/file.jpg";
//            //String filePath = "path/to/your/file.pdf";
//
//            String response = service.uploadFile(filePath);
//            System.out.println("Server Response: " + response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

