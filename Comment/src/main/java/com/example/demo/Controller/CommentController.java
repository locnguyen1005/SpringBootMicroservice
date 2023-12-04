package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.Model.Answer;
import com.example.demo.Model.Comment;
import com.example.demo.Model.Quiz;

import com.example.demo.Service.CommentService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller

public class CommentController {
	@Autowired
	private CommentService commentService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private WebClient.Builder webBuilder;

	@GetMapping("/getall")
	public Flux<Comment> getAllAnswer(){
		return commentService.getAllAnswer();
	}
	@PostMapping("/Create")
    public Mono<Comment> createQuiz(@RequestBody Comment answer) {
		
        return commentService.saveAnswer(answer);
    }


    @MessageMapping("/private-message")
    public Mono<Comment> recMessage(@Payload Comment message){
        simpMessagingTemplate.convertAndSendToUser(message.getAccountid().toString(),"/private",message);
        System.out.println(message.toString());
        return commentService.saveAnswer(message);
    }
    @MessageMapping("/sendNotification")
    @SendTo("/topic/notification/loc")
    public Comment recMessage1(@Payload Comment message){
        message.setComment("loc");
        return message;
    }
//	@PutMapping("/Edit/{id}")
//    public Flux<Answer> submit(@PathVariable Long id ,@RequestBody List<Answer> answer) {
//        return commentService.editAnswer(id,answer);
//    }

}
