package com.ApiGateway.ApiGateway.Routes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public String checkHealth() {
        String service1Health = restTemplate.getForObject("http://localhost:9091/actuator/health", String.class);
        String service2Health = restTemplate.getForObject("http://localhost:9093/actuator/health", String.class);
        String service3Health = restTemplate.getForObject("http://localhost:9092/actuator/health", String.class);
        String service4Health = restTemplate.getForObject("http://localhost:9004/actuator/health", String.class);

        return String.format("Service1 Health: %s\nService2 Health: %s\nService3 Health: %s", service1Health, service2Health, service3Health,service4Health);
    }
}
