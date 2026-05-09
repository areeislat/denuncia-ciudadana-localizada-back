package com.backend_desigeo.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Value("${users.service.url}")
    private String usersServiceUrl;

    @Value("${report.service.url}")
    private String reportServiceUrl;

    @Value("${support.service.url}")
    private String supportServiceUrl;

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    @Value("${analytics.service.url}")
    private String analyticsServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth-service", r -> r.path("/api/auth/**")
                .uri(authServiceUrl))
            .route("auth-user-service", r -> r.path("/api/users/**")
                .uri(usersServiceUrl))
            .route("report-service", r -> r.path("/api/reports/**")
                .uri(reportServiceUrl))
            .route("support-service", r -> r.path("/api/supports/**")
                .uri(supportServiceUrl))
            .route("ai-service", r -> r.path("/api/ai/**")
                .uri(aiServiceUrl))
            .route("notification-service", r -> r.path("/api/notifications/**")
                .uri(notificationServiceUrl))
            .route("analytics-service", r -> r.path("/api/analytics/**")
                .uri(analyticsServiceUrl))
            .build();
    }
}
