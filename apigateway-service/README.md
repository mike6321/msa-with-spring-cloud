# **YAML Router 설정**

## **YAML Router 설정 (1) **

```yaml
cloud:
  gateway:
    routes:
      - id: first-service
        uri: http://localhost:8081
        predicates:
          - Path=/first-service/**
      - id: second-service
        uri: http://localhost:8082
        predicates:
          - Path=/second-service/**
```

http://localhost:8000/first-service/welcome

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/204672244-6a9f8aee-0ea7-4737-9519-2fdceca21a7d.png">

http://localhost:8000/second-service/welcome

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/204672267-e8a17f55-1c5a-4234-81b2-8e1a52617d47.png">

## **YAML Router 설정 (2) **

* 필터 추가

```yaml
gateway:
  routes:
    - id: first-service
      uri: http://localhost:8081
      predicates:
        - Path=/first-service/**
      filters:
        - AddRequestHeader=first-request, first-request-header
        - AddResponseHeader=first-response, first-response-header
    - id: second-service
      uri: http://localhost:8082
      predicates:
        - Path=/second-service/**
      filters:
        - AddRequestHeader=second-request, second-request-header
        - AddResponseHeader=second-response, second-response-header
```

http://localhost:8000/first-service/message

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/204719272-76f42ac7-feb1-43cb-98d1-0bbec6c4bab0.png">

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/204719779-e6db4322-cf43-463d-8192-98451523d44e.png">

http://localhost:8000/second-service/message

<img width="726" lefalt="image" src="https://user-images.githubusercontent.com/33277588/204719490-d8403d60-d759-4583-b261-6daff087d03d.png">

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/204719878-1ec3bb43-6469-4545-8599-be346d7089d5.png">

# **Java Router 설정**

```java
@Bean
public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
    return builder.routes()
            .route(r -> r.path("/first-service/**")
                    .filters(f -> f.addRequestHeader("first-request", "first-request-header")
                            .addResponseHeader("first-response", "first-response-header"))
                    .uri("http://localhost:8081"))
            .route(r -> r.path("/second-service/**")
                    .filters(f -> f.addRequestHeader("second-request", "second-request-header")
                            .addResponseHeader("second-response", "second-response-header"))
                    .uri("http://localhost:8082"))
            .build();
}
```

http://localhost:8000/first-service/message

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/204719272-76f42ac7-feb1-43cb-98d1-0bbec6c4bab0.png">

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/204719779-e6db4322-cf43-463d-8192-98451523d44e.png">

http://localhost:8000/second-service/message

<img width="726" lefalt="image" src="https://user-images.githubusercontent.com/33277588/204719490-d8403d60-d759-4583-b261-6daff087d03d.png">

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/204719878-1ec3bb43-6469-4545-8599-be346d7089d5.png">

# **Custom Filter 추가**

```java
@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Custom Pre Filter
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom Pre Filter : request id {}", request.getId());

            // Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Custom Post Filter : response status code {}", response.getStatusCode());
            }));
        });
    }

    public static class Config {
        // Put the configuration properties
    }

}
```

```yaml
    gateway:
      routes:
        - id: first-service
          uri: http://localhost:8081
          predicates:
            - Path=/first-service/**
          filters:
            - CustomFilter
        - id: second-service
          uri: http://localhost:8082
          predicates:
            - Path=/second-service/**
          filters:
            - CustomFilter
```

http://localhost:8000/first-service/check

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/204725422-2da83f24-0450-41ee-bbfc-a391751c8777.png">

http://localhost:8000/second-service/check

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/204725530-bed5d54f-3757-4588-9d5b-16edba17a6e3.png">

# **Global Filter 추가**

```java
@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Custom Pre Filter
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global Filter baseMessage : {}", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("Global Filter Start : request id -> {}", request.getId());
            }

            // Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) {
                    log.info("Global Filter End : request status code -> {}", response.getStatusCode());
                }
            }));
        });
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

}
```

```yaml
gateway:
      default-filters:
        - name: GlobalFilter
          args:
            baseMessage: Sring Cloud Gateway Global Filter
            preLogger: true
            postLogger: true
      routes:
        - id: first-service
          uri: http://localhost:8081
          predicates:
            - Path=/first-service/**
          filters:
            - CustomFilter
        - id: second-service
          uri: http://localhost:8082
          predicates:
            - Path=/second-service/**
          filters:
            - CustomFilter
```

http://localhost:8000/second-service/check

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/204736094-af02d814-1e1e-4ae0-9a6b-244c950ff2ad.png">

# **선택적 필터 적용**

```yaml
gateway:
      default-filters:
        - name: GlobalFilter
          args:
            baseMessage: Sring Cloud Gateway Global Filter
            preLogger: true
            postLogger: true
      routes:
        - id: first-service
          uri: http://localhost:8081
          predicates:
            - Path=/first-service/**
          filters:
            - CustomFilter
        - id: second-service
          uri: http://localhost:8082
          predicates:
            - Path=/second-service/**
          filters:
            - name: CustomFilter
            - name: LoggingFilter
              args:
                baseMessage: Sring Cloud Gateway Logging Filter
                preLogger: true
                postLogger: true
```

```java
@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        GatewayFilter gatewayFilter = (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging Filter baseMessage : {}", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("Logging Filter Start : request id -> {}", request.getId());
            }

            Mono<Void> mono = chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPostLogger()) {
                    log.info("Logging Filter End : request status code -> {}", response.getStatusCode());
                }
            }));
            return mono;
        };

        return new OrderedGatewayFilter(gatewayFilter, Ordered.HIGHEST_PRECEDENCE);
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

}
```

http://localhost:8000/first-service/check

* LoggingFilter 적용 X

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/204751721-44621d0e-9b8f-457b-8e46-90668fb2b8ee.png">

http://localhost:8000/second-service/check

* LoggingFilter 적용 O

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/204751826-96375bbd-37cc-4d68-b620-3f9bd145f1e7.png">

# **LoadBalancer**

* FISRT-SERVICE Client 설정 추가

```yaml
server:
  port: 8081

spring:
  application:
    name: my-first-service

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka
```

* SECOND-SERVICE Client 설정 추가

```yaml
server:
  port: 8082

spring:
  application:
    name: my-second-service

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka
```

* Gateway routes 정보 수정 (localhost -> lb)

```yaml
gateway:
			/.../
			
      routes:
        - id: first-service
          uri: lb://MY-FIRST-SERVICE
					/.../
					
        - id: second-service
          uri: lb://MY-SECOND-SERVICE
					/.../
```

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/204759405-5b0c42ed-1b2a-4f68-ba30-571a28253be8.png">

