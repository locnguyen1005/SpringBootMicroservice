package com.example.demo.Controller;

import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import com.example.demo.Config.CustomUserDetailsService;
import com.example.demo.DTO.AccountDTO;
import com.example.demo.DTO.ProductDTO;
import com.example.demo.Entity.AccountEntity;
import com.example.demo.Event.AccountProducer;
import com.example.demo.Repository.AccountRepository;
import com.example.demo.Service.AccountService;
import com.example.demo.Service.JwtService;
import com.example.demo.utils.CommonValidate;
import com.example.demo.utils.Constant;
import com.example.demo.utils.ConstantCommon;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("Account")
@Slf4j
public class AccountController {
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private AccountProducer accountProducer;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	Gson gson = new Gson();
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	@GetMapping("/GetAll")
	public Flux<AccountDTO> getAllAccount() {
		return accountService.getAllAccount();
	}

	@PostMapping("/Create")
	public ResponseEntity<Mono<AccountDTO>> createAccount(@RequestBody String requestStr) {
		InputStream inputStream = AccountController.class.getClassLoader()
				.getResourceAsStream(Constant.JSON_CREATE_ACCOUNT);
		CommonValidate.jsonValidate(requestStr, inputStream);
		log.info(requestStr);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(accountService.createAccount(gson.fromJson(requestStr, AccountDTO.class)));
	}

	@PostMapping("/CreateProduct")
	public ResponseEntity<Mono<String>> createProduct(@RequestBody String requestStr) {
		InputStream inputStream = ProductController.class.getClassLoader()
				.getResourceAsStream(com.example.demo.Utils.Constant.JSON_Product);
		CommonValidate.jsonValidate(requestStr, inputStream);

		return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createProduct(requestStr));
	}
	@PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AccountDTO authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }


    }

	
}