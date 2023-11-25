package com.example.demo.Service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Common.CommonException;
import com.example.demo.Config.Config;
import com.example.demo.DTO.AccountDTO;

import com.example.demo.Entity.AccountEntity;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.utils.ConstantCommon;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired 
	private ModelMapper mapper;
	@Autowired
	Gson gson = new Gson();
	@Autowired
    private PasswordEncoder passwordEncoder;
	@Autowired
    private JwtService jwtService;

	
	public Flux<AccountDTO> getAllAccount() {
		return accountRepository.findAll().map(accountEntity -> mapper.map(accountEntity , AccountDTO.class)).switchIfEmpty(Mono.error(new Exception("Account Empty")));
	}
	public Mono<Boolean> checkDuplicate(AccountDTO accountDTO){
		return accountRepository.findByEmail(accountDTO.getEmail())
				.flatMap(accountEntity -> Mono.just(true))
				.switchIfEmpty(Mono.just(Boolean.FALSE));
	}
	public Mono<AccountDTO> createAccount(AccountDTO accountDTO){
		if((checkDuplicate(accountDTO).block()).equals(Boolean.TRUE)) {
			 return Mono.error(new CommonException(accountDTO.getEmail(), "Account duplicate", HttpStatus.BAD_REQUEST));
		}
		else {
			accountDTO.setPassword(passwordEncoder.encode(accountDTO.getPassword()));
			return Mono.just(accountDTO)
					.map(newAccountDTO -> mapper.map(newAccountDTO, AccountEntity.class))
					.flatMap(account -> accountRepository.save(account))
					.map(accountEntity -> mapper.map(accountEntity, AccountDTO.class));
		}
	}
	public Mono<AccountDTO> deleteAccount(AccountDTO accountDTO){
		if((checkDuplicate(accountDTO).block()).equals(Boolean.TRUE)) {
			 return Mono.just(accountDTO)
					 .map(account -> mapper.map(account, AccountEntity.class))
					 .map(accountEntity -> accountRepository.deleteById(accountEntity.getId()))
					 .map((accountEntity -> mapper.map(accountEntity, AccountDTO.class)));
		}
		else {

			return Mono.error(new Exception());
		}
	}


    public Mono<AccountEntity> findAccount(String accountDTO){
		return accountRepository.findByEmail(accountDTO);
	}
    
}
