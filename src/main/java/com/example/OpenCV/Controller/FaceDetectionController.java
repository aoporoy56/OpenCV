package com.example.OpenCV.Controller;


import com.example.OpenCV.Entity.Image;
import com.example.OpenCV.Entity.Images;
import com.example.OpenCV.Entity.Search;
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
    public String match(@RequestBody String image, Model model) throws Exception {

        String[] base64String2 = {image};

        String[] base64String = {"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"};

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