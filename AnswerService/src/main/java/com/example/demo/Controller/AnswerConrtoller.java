package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.Model.Answer;

import com.example.demo.Service.AnswerService;

import jakarta.ws.rs.PUT;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AnswerConrtoller {
	@Autowired
	private AnswerService answerService;
	
	public Flux<Answer> getAllAnswer(){
		return answerService.getAllAnswer();
	}
	@PostMapping("/Answer")
    public Mono<Answer> createQuiz(@RequestBody Answer answer) {
		answer.setResult(0l);
        return answerService.saveAnswer(answer);
    }
	@PutMapping("/Answer")
    public Mono<Answer> editQuiz(@RequestBody Answer answer) {
        return answerService.saveAnswer(answer);
    }
	
}
