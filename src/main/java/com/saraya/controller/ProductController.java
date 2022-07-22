package com.saraya.controller;

import com.saraya.dto.ProductDto;
import com.saraya.entity.Product;
import com.saraya.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping
    public Flux<ProductDto> getProducts(){
        return service.getAllProducts();
    }
    @GetMapping("/{id}")
    public Mono<ProductDto> getProduct(@PathVariable String id){
        return  service.getProductById(id);
    }

    @GetMapping("/price")
    public Mono<Product> getProductByPrice(@RequestParam("minprice")double minprice ,
                                           @RequestParam("maxprice") double maxprice){
       return service.getProductPriceRang(minprice , maxprice);
    }
    @PostMapping
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono){
        return service.saveProduct(productDtoMono);
    }
    @PutMapping("/update/{id}")
    public Mono<ProductDto> updateProduct(@PathVariable String id, @RequestBody Mono<ProductDto> productDtoMono){
       return service.updateProduct(id, productDtoMono);
    }
    @DeleteMapping("/delete/{id}")
    public Mono<Void> deleteProduct( @PathVariable  String id){
      return   service.deleteProduct(id);
    }
}
