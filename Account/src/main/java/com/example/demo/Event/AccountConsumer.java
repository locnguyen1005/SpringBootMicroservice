package com.example.demo.Event;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.AccountDTO;
import com.example.demo.Service.AccountService;
import com.example.demo.Utils.Constant;
import com.example.demo.utils.ConstantCommon;
import com.google.gson.Gson;


import lombok.extern.slf4j.Slf4j;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Service
@Slf4j
public class AccountConsumer {
	Gson gson = new Gson();
	@Autowired
	AccountService accountService;
	
	
}
