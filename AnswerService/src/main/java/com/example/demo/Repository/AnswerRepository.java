package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.demo.Model.Answer;


public interface AnswerRepository extends ReactiveMongoRepository<Answer, String>{

}
