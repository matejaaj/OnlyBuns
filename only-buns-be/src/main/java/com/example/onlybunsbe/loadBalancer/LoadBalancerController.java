package com.example.onlybunsbe.loadBalancer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/proxy")
public class LoadBalancerController {

    private final List<String> instances = List.of(
            "http://localhost:8081",
            "http://localhost:8082"
    );

    private final RoundRobinBalancer balancer = new RoundRobinBalancer(instances);
    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping("/**")
    public ResponseEntity<?> forwardAll(HttpMethod method,
                                        HttpServletRequest request,
                                        @RequestBody(required = false) String body,
                                        @RequestHeader HttpHeaders incomingHeaders) {

        int attempts = 0;
        int maxAttempts = instances.size();

        while (attempts < maxAttempts) {

            String instance = balancer.getNextInstance();
            String path = request.getRequestURI().replace("/proxy", "");
            String forwardUrl = instance + path;

            try {
                System.out.println("Prosledjujem zahtev na instancu: " + instance);
                HttpHeaders headers = new HttpHeaders();
                headers.putAll(incomingHeaders);
                HttpEntity<String> entity = new HttpEntity<>(body, headers);

                ResponseEntity<String> response = restTemplate.exchange(forwardUrl, method, entity, String.class);
                return response;

            } catch (Exception e) {
                attempts++;
            }
        }

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("All backend instances are down.");
    }
}
