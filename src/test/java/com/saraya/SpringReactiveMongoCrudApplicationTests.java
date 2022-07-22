package com.saraya;

import com.saraya.controller.ProductController;
import com.saraya.dto.ProductDto;
import com.saraya.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
public class SpringReactiveMongoCrudApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ProductService productService;

	@Test
	public void addProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(
				new ProductDto("102", "mobile", 1, 797.00));
		when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);

		webTestClient.post().uri("/products").
				body(Mono.just(productDtoMono), ProductDto.class).exchange().expectStatus().isOk();
	}
	@Test
	public void getProducts(){
		Flux<ProductDto> productDtoFlux =
				Flux.just(new ProductDto("103", "mobile", 1, 8000.00),
				new ProductDto("104", "car", 2,9000.00));

		when(productService.getAllProducts()).thenReturn(productDtoFlux);
	Flux<ProductDto> responseBody=	webTestClient.get().uri("/products").exchange().
				expectStatus().isOk().returnResult(ProductDto.class)
				.getResponseBody();
		StepVerifier.create(responseBody).expectSubscription()
				.expectNext(new ProductDto("103", "mobile", 1, 8000.00))
				.expectNext(new ProductDto("104", "car", 2,9000.00)).verifyComplete();
	}

//@Test
//	public void getProductByPrice(){
//		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("103", "mobile", 1, 9000.00));
//		when(productService.getProductPriceRang(700.00, 9000.00));
//
//		Flux<ProductDto> responseBody = webTestClient.get().uri("/products/?minprice=2000&maxprice=5000")
//				.exchange().expectStatus().isOk().
//		returnResult(ProductDto.class).getResponseBody();
//		StepVerifier.create(responseBody).expectFusion(700, 5000).verifyComplete();
//	}
	@Test
	public void getProductById(){
		Mono<ProductDto> productDtoMono =Mono.just(new ProductDto("104", "car", 1, 700.00));
		when(productService.getProductById(any())).thenReturn(productDtoMono);

		Flux<ProductDto> responseBody= webTestClient.get().uri("/products/102").
				exchange().expectStatus().isOk().
				returnResult(ProductDto.class).getResponseBody();
		StepVerifier.create(responseBody).expectSubscription()
				.expectNextMatches(p->p.getName().equals("car")).verifyComplete();
	}
	@Test
	public void  updateProduct(){
		Mono<ProductDto> productDtoMono = Mono.just(
				new ProductDto("103", "volveWagen", 1, 8000.00));

		when(productService.updateProduct("102", productDtoMono)).thenReturn(productDtoMono);

		webTestClient.put().uri("/products/update/102")
				.body(Mono.just(productDtoMono), ProductDto.class)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void deletePRoduct(){

		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("104", "car", 1, 797.00));

		given(productService.deleteProduct(any())).willReturn(Mono.empty());
		webTestClient.delete().uri("/products/delete/104").exchange().expectStatus().isOk();
	}

}
