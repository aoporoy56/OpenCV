package com.example.OpenCV.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MyService {

    private final WebClient webClient;

    @Autowired
    public MyService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void callApi() {
        // Make API call using WebClient
        String apiUrl = "https://api.example.com/endpoint";
        String response = webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Process the API response
        System.out.println(response);
    }
}
