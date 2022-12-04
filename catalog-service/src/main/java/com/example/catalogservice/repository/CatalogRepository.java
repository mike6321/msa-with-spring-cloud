package com.example.catalogservice.repository;

import com.example.catalogservice.entity.CatalogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CatalogRepository extends CrudRepository<CatalogEntity, Long> {

    @Transactional(readOnly = true)
    CatalogEntity findByProductId(String productId);

}
