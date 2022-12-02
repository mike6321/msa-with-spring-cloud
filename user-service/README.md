## Users Microservice와 Spring Cloud Gateway 연동

### Security 설정

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/205194103-f7ea6be1-e29e-4ecd-9b96-07b35c1ddf4b.png">

### API Gateway 설정

```yaml
        - id: user-service
          uri: lb://USER-SERVICE
          predicates:
            - Path=/user-service/**
```

### URL

http://localhost:8000/user-service/health_check

<img width="726" alt="image" src="https://user-images.githubusercontent.com/33277588/205194018-31c2101c-c6ce-4cf6-910c-9384f053f6c0.png">