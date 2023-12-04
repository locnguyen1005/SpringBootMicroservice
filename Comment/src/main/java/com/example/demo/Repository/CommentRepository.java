package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.Comment;

@Repository
public interface CommentRepository extends ReactiveMongoRepository<Comment, String>{

}
