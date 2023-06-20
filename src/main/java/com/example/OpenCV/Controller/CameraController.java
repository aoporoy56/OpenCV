package com.example.OpenCV.Controller;

import com.github.sarxos.webcam.Webcam;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
public class CameraController {

    @GetMapping("live")
    public String index() {
        return "live_feed"; // Assuming your HTML file is named "index.html"
    }

    @PostMapping("/take-picture")
    @ResponseBody
    public String takePicture(MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            try {
                byte[] bytes = imageFile.getBytes();

                // Specify the directory to save the image
                String directoryPath = "/";

                // Create the directory if it doesn't exist
                File directory = new File(directoryPath);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Generate a unique filename for the image
                String filename = "asdf.jpg";

                // Save the image file
                File file = new File(directoryPath, filename);
                try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                    outputStream.write(bytes);
                }

                // Return a success message or the filename if required
                return "Image saved successfully with filename: " + filename;
            } catch (IOException e) {
                e.printStackTrace();
                return "Error saving the image.";
            }
        } else {
            return "No image data received.";
        }
    }

}
