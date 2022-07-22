package com.saraya.service;


import com.saraya.dto.ProductDto;
import com.saraya.entity.Product;
import com.saraya.repository.ProductRepository;
import com.saraya.utils.AppUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReactiveMongoTemplate template;

    public Flux<ProductDto> getAllProducts(){
      return productRepository.findAll().map(AppUtils::ConvertEntityToDto);

    }
    public Mono<ProductDto> findProductById(String id){
        return template.findById(id, ProductDto.class)
                .log("From template");
    }
    public Mono<ProductDto> getProductById( String id){
        return productRepository.findById(id).map(AppUtils::ConvertEntityToDto).log(
                   "From repository"
        );
    }
    public Mono<Product> getProductPriceRang(double minprice, double maxprice ){
        return productRepository.findByPriceBetween(Range.closed(minprice,maxprice));
                //productRepository.findByPriceBetween(Range.closed(minprice,minprice));
    }
//    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono){
//        return template.save(productDtoMono);
//    }
// public Flux<ProductDto> findAll(){
//       return (Flux<ProductDto>)productRepository.findAll().
//               toStream().map(product->modelMapper.map(product, ProductDto.class));
//    }
//
//    public Mono<ProductDto> getProductById(String id){
//        Mono<Product> produ = productRepository.findById(id);
//ProductDto productDto = modelMapper.map(produ, ProductDto.class);
//        return  Mono.just(productDto);
//    }
//    public Flux<ProductDto> getProductByPrice(double min, double max){
//        Flux<Product> productFlux = productRepository.findByPriceBetween(Range.closed(min, max));
//       return modelMapper.map(productFlux, (Type) ProductDto.class);
//    }
//    public

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono){
       return productDtoMono.map(AppUtils::ConverDToToEntity).
               flatMap(productRepository::save).map(AppUtils::ConvertEntityToDto);
    }
    public Mono<ProductDto> updateProduct(String id, Mono<ProductDto> productDtoMono){
      return  productRepository.findById(id).//find data from db
              flatMap(p->productDtoMono.//convert productDtoMono to Entity
              map(AppUtils::ConverDToToEntity)
                .doOnNext(e->e.setId(id))).//set the id as it is then
              flatMap(productRepository::save)//save the updated data
                .map(AppUtils::ConvertEntityToDto);//now convert entity to dto and return dto
    }
    public Mono<Void> deleteProduct(String id){
         return productRepository.deleteById(id);
    }
}
