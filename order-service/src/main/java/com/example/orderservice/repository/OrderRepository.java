package com.example.orderservice.repository;

import com.example.orderservice.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

    @Transactional(readOnly = true)
    OrderEntity findByOrderId(String orderId);

    @Transactional(readOnly = true)
    Iterable<OrderEntity> findByUserId(String userId);

}
