package com.jeremylee.insta_gateway_service.config;

import com.jeremylee.insta_gateway_service.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGatewayConfig {

        @Autowired
        AuthenticationFilter filter;

        @Bean
        public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
                Function<PredicateSpec, Buildable<Route>> routeFunction = predicateSpec -> predicateSpec
                                // can match on headers, host, request method, query parameter
                                // matches on the path
                                .path("/gets")
                                .filters(gatewayFilterSpec -> gatewayFilterSpec
                                                // can be Autheication Header
                                                .addRequestHeader("MyHeader", "MyURI")
                                                .addRequestParameter("Param", "MyValue"))
                                // can be URL of microservices
                                .uri("http://httpbin.org:80");

                Function<PredicateSpec, Buildable<Route>> routeMedia = p -> p.path("/hello/**")
                                // currency-exchange -> talk to Eureka, find the location of this service
                                // lb -> and load balancing between instances which are returned
                                .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(filter))
                                .uri("lb://insta-media-service");

                Function<PredicateSpec, Buildable<Route>> routePost = p -> p.path("/post/**")
                                // currency-exchange -> talk to Eureka, find the location of this service
                                // lb -> and load balancing between instances which are returned
                                .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(filter))
                                .uri("lb://insta-post-service");

                Function<PredicateSpec, Buildable<Route>> routeUser = p -> p.path("/user/**")
                                // currency-exchange -> talk to Eureka, find the location of this service
                                // lb -> and load balancing between instances which are returned
                                .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(filter))
                                .uri("lb://insta-user-service");

                Function<PredicateSpec, Buildable<Route>> routeAuth = p -> p.path("/auth/**")
                                // currency-exchange -> talk to Eureka, find the location of this service
                                // lb -> and load balancing between instances which are returned
                                .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(filter))
                                .uri("lb://insta-auth-service");

                Function<PredicateSpec, Buildable<Route>> routeLink = p -> p.path("/api/links/**")
                                .filters(gatewayFilterSpec -> gatewayFilterSpec.filter(filter))
                                .uri("lb://idea-link-service");

                return builder.routes()
                                .route(routeFunction)
                                .route(routeMedia)
                                .route(routePost)
                                .route(routeUser)
                                .route(routeAuth)
                                .route(routeLink)
                                .build();

        }

}
