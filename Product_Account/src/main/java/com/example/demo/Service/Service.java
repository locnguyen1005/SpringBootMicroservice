package com.example.demo.Service;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.Common.CommonException;
import com.example.demo.Model.AccountRegister;
import com.example.demo.Model.Account_Product;
import com.example.demo.Model.Product;
import com.example.demo.Repository.Product_Account_Reopository;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@org.springframework.stereotype.Service
public class Service {
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private Product_Account_Reopository repository;
	@Autowired
	private WebClient.Builder webBuilder;
	
	public Flux<Account_Product> getAllProduct(){
		return repository.findAll().map(Product_account -> modelMapper.map(Product_account, Account_Product.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
	public Flux<AccountRegister> getAllProductAccountRegistered(Long id){
		
		return repository.findByAccountId(id).switchIfEmpty(Flux.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
	public Mono<AccountRegister> registerProduct(AccountRegister accountRegister){
		Mono<Product> resultProduct = webBuilder.build().get()
                .uri("http://localhost:8889/product/" + accountRegister.getProductId())
                .retrieve()
                .bodyToMono(Product.class);
		 LocalDateTime currentDate = LocalDateTime.now();
		 Flux<AccountRegister> allProductById = repository.findByProductId(accountRegister.getProductId());
		 if(currentDate.compareTo(resultProduct.block().getStarttime()) < 0 && resultProduct.block().getMaxuser() < allProductById.count().block() ) {
			 return Mono.error(new Exception());
		 }
		 
		return Mono.just(accountRegister);
	}
}
