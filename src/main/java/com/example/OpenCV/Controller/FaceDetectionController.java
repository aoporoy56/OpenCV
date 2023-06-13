package com.example.OpenCV.Controller;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FaceDetectionController {





    private final String faceCascadePath = "haarcascade_frontalface_alt.xml";

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/detect-faces")
    public String detectFaces(@RequestParam("imageFile") MultipartFile imageFile, Model model) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        try {
            // Load the face cascade classifier
            CascadeClassifier faceCascade = new CascadeClassifier(faceCascadePath);

            // Load the image using OpenCV
            Mat image = Imgcodecs.imdecode(new MatOfByte(imageFile.getBytes()), Imgcodecs.IMREAD_COLOR);

            // Convert the image to grayscale
            Mat grayImage = new Mat();
            Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

            // Detect faces in the image
            MatOfRect faces = new MatOfRect();
            faceCascade.detectMultiScale(grayImage, faces);

            // Set the detected faces in the model
            model.addAttribute("faces", faces.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "result";
    }
}
