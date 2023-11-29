package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.Model.Answer;
import com.example.demo.Repository.AnswerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AnswerService {
	@Autowired
	private AnswerRepository answerRepository;
	@Autowired
	private WebClient.Builder webBuilder;
	
	public Flux<Answer> getAllAnswer(){
		return  answerRepository.findAll();
	}
	
	public Mono<Answer> saveAnswer(Answer answer){
		return answerRepository.save(answer);
	}
	
	public Mono<Answer> editAnswer(Answer answer){

		return answerRepository.save(answer);
	}
}
