package com.example.OpenCV.Controller;


import com.example.OpenCV.Entity.Image;
import com.example.OpenCV.Entity.Images;
import com.example.OpenCV.Entity.Search;
import com.example.OpenCV.Entity.TwoImage;
import com.github.sarxos.webcam.Webcam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@Controller
public class FaceDetectionController {

    private WebClient webClient;

    @Autowired
    public void MyController(WebClient webClient) {
        this.webClient = webClient;
    }

    public FaceDetectionController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/detect-faces")
    public Mono<String> detectFaces(@RequestParam("image") MultipartFile file, Model model) throws Exception  {
        byte[] image = file.getBytes();
        String base64String = Base64.getEncoder().encodeToString(image);
        Search search = new Search("stringstringstringstringstringstring",0.7,"FAST");
        Image image1 = new Image(base64String,search);

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("X-API-Key", "ecVlq2zNWIwMzU5OWQtMjk1Ni00NjZjLWFkNGYtZTI2NjE3NTg0YWFi");
        String apiUrl = "https://us.opencv.fr/detect";

        return webClient.post()
                .uri(apiUrl)
                .headers((httpHeaders -> httpHeaders.addAll((headers))))
                .body(
                        BodyInserters.fromValue(image1)
                )
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    // Process the API response
                    System.out.println(response);
                    model.addAttribute("response", response);
                })
                .then(Mono.just("result"));
    }

    @PostMapping("/match-faces")
    public Mono<String> detectFaces(@RequestParam("image") MultipartFile file, @RequestParam("image2") MultipartFile file2, Model model) throws Exception  {
        byte[] image = file.getBytes();
        String[] base64String = {Base64.getEncoder().encodeToString(image)};

        byte[] image2 = file2.getBytes();
        String[] base64String2 = {Base64.getEncoder().encodeToString(image2)};
        Images image1 = new Images(base64String,base64String2,"ACCURATE");



        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("X-API-Key", "ecVlq2zNWIwMzU5OWQtMjk1Ni00NjZjLWFkNGYtZTI2NjE3NTg0YWFi");
        String apiUrl = "https://us.opencv.fr/compare";

        return webClient.post()
                .uri(apiUrl)
                .headers((httpHeaders -> httpHeaders.addAll((headers))))
                .body(
                        BodyInserters.fromValue(image1)
                )
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    // Process the API response
                    System.out.println(response);
                    model.addAttribute("response", response);
                })
                .then(Mono.just("result"));
    }


    @PostMapping("/match")
    @ResponseBody
    public String match(@RequestBody TwoImage im, Model model) throws Exception {

        String[] base64String2 = {im.getImage()};
        String[] base64String = {im.getImage2()};
        System.out.println(im.getImage2());


        Images image1 = new Images(base64String, base64String2, "ACCURATE");

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("X-API-Key", "ecVlq2zNWIwMzU5OWQtMjk1Ni00NjZjLWFkNGYtZTI2NjE3NTg0YWFi");
        String apiUrl = "https://us.opencv.fr/compare";

        Mono<String> responseMono = webClient.post()
                .uri(apiUrl)
                .headers((httpHeaders -> httpHeaders.addAll((headers))))
                .body(
                        BodyInserters.fromValue(image1)
                )
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    // Process the response here
                    String processedResponse = response + " (processed)";
                    System.out.println("Response: " + processedResponse);
                    return processedResponse;
                });
                    responseMono.subscribe(
                response -> {
                    System.out.println("Response: " + response);
                    // Use the processed response here
                },
                error -> {
                    System.err.println("Error occurred: " + error.getMessage());
                    // Handle the error here
                }
        );

        return apiUrl;
    }


}