package com.example.demo.Repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.AccountEntity;

import reactor.core.publisher.Mono;

public interface AccountRepository extends R2dbcRepository<AccountEntity, Long>{
	Mono<AccountEntity> findByEmail(String email);
}
