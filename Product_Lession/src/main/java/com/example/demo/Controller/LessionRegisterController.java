package com.example.demo.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.Event.LessionProductProducer;
import com.example.demo.Model.Account;
import com.example.demo.Model.Lession;
import com.example.demo.Model.LessionRegister;
import com.example.demo.Model.Product;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class LessionRegisterController {
	@Autowired
	private WebClient.Builder webBuilder;
	
	@Autowired
	private LessionProductProducer lessionProductProducer;
	@GetMapping("/demo")
	public String demo(@RequestBody LessionRegister lession){
		Mono<Product> resultProduct = webBuilder.build().get()
                .uri("http://localhost:8889/product/"+lession.getProductId())
                .retrieve()
                .bodyToMono(Product.class);
//		Flux<Account> resultAccount = webBuilder.build().get()
//                .uri("http://localhost:8888/Account/GetAll")
//                .retrieve()
//                .bodyToFlux(Account.class);
		
//		Map<Long, Account> personMap = resultAccount.toStream()
//                .collect(Collectors.toMap(Account::getId, account -> account));
//		
//		List<Account_Product> mergedDataList = resultProduct.toStream()
//                .map(person -> new Account_Product(person.getName(), personMap.get(person.getAccountid()).getEmail()))
//                .collect(Collectors.toList());
		return null;
	}
}