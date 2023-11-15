package com.example.demo.Service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.Common.CommonException;

import com.example.demo.DTO.LessionDTO;

import com.example.demo.Entity.LessionEntity;

import com.example.demo.Repository.LessionRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class LesssionService {
	private static final String Class = null;
	@Autowired
	private LessionRepository lessionRepository;
	@Autowired 
	private ModelMapper mapper;
	public Flux<LessionDTO> getAllAccount() {
		return lessionRepository.findAll().map(lessionEntity -> mapper.map(lessionEntity , LessionDTO.class)).switchIfEmpty(Mono.error(new Exception("Lession Empty")));
	}
	public Mono<Boolean> checkDuplicate(LessionDTO lessionDTO){
		return lessionRepository.findByTitle(lessionDTO.getTitle())
				.flatMap(lessionEntiy -> Mono.just(true))
				.switchIfEmpty(Mono.just(Boolean.FALSE));
	}
	public Mono<LessionDTO> createLession(LessionDTO lessionDTO){
		log.info(lessionDTO.getProductId() + "description");
		if((checkDuplicate(lessionDTO).block()).equals(Boolean.TRUE)) {
			 return Mono.error(new CommonException(lessionDTO.getTitle(), "Name lession duplicate", HttpStatus.BAD_REQUEST));
		}
		else {

			return Mono.just(lessionDTO)
					.map(newLession -> mapper.map(newLession, LessionEntity.class))
					.flatMap(newLessionEntity -> lessionRepository.save(newLessionEntity))
					.map(accountEntity -> mapper.map(accountEntity, LessionDTO.class));
		}
	}
	public Mono<LessionDTO> deleteAccount(LessionDTO lessionDTO){
		if((checkDuplicate(lessionDTO).block()).equals(Boolean.TRUE)) {
			 return Mono.just(lessionDTO)
					 .map(lession -> mapper.map(lession, LessionEntity.class))
					 .map(lessionEntity -> lessionRepository.deleteById(lessionEntity.getId()))
					 .map((accountEntity -> mapper.map(accountEntity, LessionDTO.class)));
		}
		else {

			return Mono.error(new Exception());
		}
	}
}
