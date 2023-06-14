package com.example.OpenCV.Controller;


import java.util.Base64;

import com.example.OpenCV.Entity.Image;
import com.example.OpenCV.Entity.Images;
import com.example.OpenCV.Entity.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
}
