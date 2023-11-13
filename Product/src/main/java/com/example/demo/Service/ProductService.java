package com.example.demo.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice.Return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.Common.CommonException;
import com.example.demo.DTO.ProductDTO;
import com.example.demo.Entity.ProductEntity;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.event.ProductProducer;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ProductProducer producer;
	public Flux<ProductDTO> getAllProduct(){
		return productRepository.findAll().map(ProductDTO -> modelMapper.map(ProductDTO, ProductDTO.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
	public Mono<Boolean> checkDuplicate(ProductDTO productDTO){
		return productRepository.findByName(productDTO.getName())
				.map(product ->  Boolean.TRUE)
				.switchIfEmpty(Mono.just(Boolean.FALSE));
	}
	public Mono<ProductDTO> createProduct(ProductDTO productDTO){
		ProductDTO newproduDto = new ProductDTO();
		newproduDto.setAccountid(productDTO.getAccountid());
		newproduDto.setName(productDTO.getName());
		if(checkDuplicate(newproduDto).block().equals(Boolean.TRUE)) {
			log.info(newproduDto.toString());
			return Mono.error(new CommonException(newproduDto.getName(), "The course name already exists", HttpStatus.BAD_REQUEST));
		}
		else {
			log.info(newproduDto.toString());
			return Mono.just(newproduDto)
					.map(productdto -> modelMapper.map(productdto, ProductEntity.class))
					.flatMap(product -> productRepository.save(product))
					.map(productentity -> modelMapper.map(productentity, ProductDTO.class))
					.doOnSubscribe(dto -> log.info("susscess"));
		}
	}
	public Mono<ProductDTO> finđById(Long productDTOID){
		return productRepository.findById(productDTOID).map(ProductDTO -> modelMapper.map(ProductDTO, ProductDTO.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
}