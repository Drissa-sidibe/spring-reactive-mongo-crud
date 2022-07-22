package com.saraya.repository;

import com.saraya.entity.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, String> {


    Mono<Product> findByPriceBetween(Range<Double> closed);

}
