package com.example.demo.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.Model.Account;
import com.example.demo.Model.Account_Product;
import com.example.demo.Model.Product;

import liquibase.integration.servlet.LiquibaseJakartaStatusServlet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class Controller {

	@Autowired
	private WebClient.Builder webBuilder;
	
	@GetMapping("/demo")
	public List<Account_Product> demo(){
		Flux<Product> resultProduct = webBuilder.build().get()
                .uri("http://localhost:8889/product/getall")
                .retrieve()
                .bodyToFlux(Product.class);
		Flux<Account> resultAccount = webBuilder.build().get()
                .uri("http://localhost:8888/Account/GetAll")
                .retrieve()
                .bodyToFlux(Account.class);
		
		Map<Long, Account> personMap = resultAccount.toStream()
                .collect(Collectors.toMap(Account::getId, account -> account));
		
		List<Account_Product> mergedDataList = resultProduct.toStream()
                .map(person -> new Account_Product(person.getName(), personMap.get(person.getAccountid()).getEmail()))
                .collect(Collectors.toList());
		return mergedDataList;
	}
	@PostMapping("/Post")
	public Mono<String> registerCourse(){
		Flux<Product> resultProduct = webBuilder.build().get()
                .uri("http://localhost:8889/product/getall")
                .retrieve()
                .bodyToFlux(Product.class);
		Flux<Account> resultAccount = webBuilder.build().get()
                .uri("http://localhost:8888/Account/GetAll")
                .retrieve()
                .bodyToFlux(Account.class);
		
		Map<Long, Account> personMap = resultAccount.toStream()
                .collect(Collectors.toMap(Account::getId, account -> account));
		
		List<Account_Product> mergedDataList = resultProduct.toStream()
                .map(person -> new Account_Product(person.getName(), personMap.get(person.getAccountid()).getEmail()))
                .collect(Collectors.toList());
		return null;
	}
}