package com.example.demo.Controller;

import java.io.InputStream;
import java.util.List;

import javax.management.loading.PrivateClassLoader;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.Service.ProductService;
import com.example.demo.Utils.Constant;
import com.example.demo.utils.CommonValidate;
import com.example.demo.utils.ConstantCommon;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {
	@Autowired
	private ProductService productService;
	@Autowired
	Gson gson;
	@GetMapping("/getall")
	public Flux<ProductDTO> getAllProduct(){
		return productService.getAllProduct();
	}
	@PostMapping("/createproduct")
	public ResponseEntity<Mono<ProductDTO>> createProduct(@RequestBody String product){
		log.info(product);
		InputStream inputStream = ProductController.class.getClassLoader().getResourceAsStream(Constant.JSON_Product);
		CommonValidate.jsonValidate(product, inputStream);
		log.info("1");
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(gson.fromJson(product, ProductDTO.class)));
	}
}
