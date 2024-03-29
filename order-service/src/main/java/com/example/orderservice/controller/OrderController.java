package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.messagequeue.KafkaProducer;
import com.example.orderservice.messagequeue.OrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {

    private final Environment environment;
    private final OrderService orderService;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @GetMapping("/health_check")
    public String status() {
        String port = environment.getProperty("local.server.port");
        return String.format("It's working in order-service on port %s", port);
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createUser(@PathVariable("userId") String userId,
                                                    @RequestBody RequestOrder orderDetail) {
        log.info("Before add orders data.");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderDto orderDto = modelMapper.map(orderDetail, OrderDto.class);
        /**
         * jpa
         * */
        orderDto.setUserId(userId);
        OrderDto createdOrder = orderService.createOrder(orderDto);
        ResponseOrder responseOrder = modelMapper.map(createdOrder, ResponseOrder.class);

        /**
         * kafka
         * */
//        orderDto.setOrderId(UUID.randomUUID().toString());
//        orderDto.setTotalPrice(orderDetail.getQty() * orderDetail.getUnitPrice());

//        ResponseOrder responseOrder = modelMapper.map(orderDto, ResponseOrder.class);

//        kafkaProducer.send("example-catalog-topic", orderDto);
//        orderProducer.send("orders", orderDto);

        log.info("After add orders data.");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId) {
        log.info("Before retrieve orders data.");
        Iterable<OrderEntity> orderEntities = orderService.getOrderByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        orderEntities.forEach(order -> result.add(new ModelMapper().map(order, ResponseOrder.class)));
        log.info("After retrieve orders data.");

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
