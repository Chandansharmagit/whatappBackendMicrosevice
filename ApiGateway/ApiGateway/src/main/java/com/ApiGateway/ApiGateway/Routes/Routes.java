package com.ApiGateway.ApiGateway.Routes;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@CrossOrigin
public class Routes {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route for MICROSERVICE_WEB
                .route("MICROSERVICE_WEB", r -> r.path("/naming/**")
                        .uri("http://localhost:9091"))  // Direct URI for MICROSERVICE_WEB

                // Route for MICROSERVICE_WEB2 (GET endpoint)
                .route("MICROSERVICE_WEB2_GET", r -> r.path("/get/**")
                        .uri("http://localhost:9093"))  // Direct URI for MICROSERVICE_WEB2

                // Route for MICROSERVICE_WEB2 (GET endpoint)
                .route("Authentications", r -> r.path("/auth/**")
                        .uri("http://localhost:9004"))  // Direct URI for MICROSERVICE_WEB2

                // Route for MICROSERVICE_WEB2 (POST endpoint)
                .route("MICROSERVICE_WEB2_CREATE", r -> r.path("/adding/**")
                        .uri("http://localhost:9093"))  // Direct URI for MICROSERVICE_WEB2

                // Route for MICROSERVICE_WEB2 (GET by ID endpoint)
                .route("MICROSERVICE_WEB2_GET_BY_ID", r -> r.path("/get/{id}/**")
                        .uri("http://localhost:9093"))  // Direct URI for MICROSERVICE_WEB2

                .build();
    }
}
