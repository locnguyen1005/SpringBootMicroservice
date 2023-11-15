package com.example.demo.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice.Return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.Common.CommonException;
import com.example.demo.DTO.ProductDTO;
import com.example.demo.Entity.ProductEntity;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.event.ProductProducer;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ModelMapper modelMapper;
	
//	@Value("${google.oauth.callback.uri}")
//	private String CALLBACK_URI;
//
//	@Value("${google.secret.key.path}")
//	private Resource gdSecretKeys;
//
//	@Value("${google.credentials.folder.path}")
//	private Resource credentialsFolder;
//	
//	@Value("${google.service.account.key}")
//	private Resource serviceAccountKey;
//	
//	private GoogleAuthorizationCodeFlow flow;
//	
//	private static final String USER_IDENTIFIER_KEY = "MY_DUMMY_USER";
//	private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
//	private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
//
//	private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE,
//			"https://www.googleapis.com/auth/drive.install");
	
	private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE,
			"https://www.googleapis.com/auth/drive.install");

	private static final String USER_IDENTIFIER_KEY = "MY_DUMMY_USER";
    
    @Value("${google.oauth.callback.uri}")
	private String CALLBACK_URI;
	@Value("${google.secret.key.path}")
	private Resource gdSecretKeys;
	@Value("${google.credentials.folder.path}")
	private Resource credentialsFolder;

	
	private GoogleAuthorizationCodeFlow flow;
	
	@Autowired
	private ProductProducer producer;
	@PostConstruct
	public void init() throws Exception {
		GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(gdSecretKeys.getInputStream()));
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, secrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(credentialsFolder.getFile())).build();
	}
	public Flux<ProductDTO> getAllProduct(){
		return productRepository.findAll().map(ProductDTO -> modelMapper.map(ProductDTO, ProductDTO.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
	public Mono<Boolean> checkDuplicate(ProductDTO productDTO){
		return productRepository.findByName(productDTO.getName())
				.map(product ->  Boolean.TRUE)
				.switchIfEmpty(Mono.just(Boolean.FALSE));
	}
	public Mono<ProductDTO> createProduct(ProductDTO productDTO) throws IOException{
		ProductDTO newproduDto = new ProductDTO();
		newproduDto.setAccountid(productDTO.getAccountid());
		newproduDto.setName(productDTO.getName());
		
		Credential cred = flow.loadCredential(USER_IDENTIFIER_KEY);

		Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, cred)
				.setApplicationName("googledrivespringbootexample").build();
		
		File file = new File();
		file.setName(productDTO.getName());
		file.setMimeType("application/vnd.google-apps.folder");
		file=drive.files().create(file).setFields("id").execute();
		newproduDto.setFolder(file.getId());
		log.info(file.getId());
		if(checkDuplicate(newproduDto).block().equals(Boolean.TRUE)) {
			log.info(newproduDto.toString());
			return Mono.error(new CommonException(newproduDto.getName(), "The course name already exists", HttpStatus.BAD_REQUEST));
		}
		else {
			log.info(newproduDto.toString());
			return Mono.just(newproduDto)
					.map(productdto -> modelMapper.map(productdto, ProductEntity.class))
					.flatMap(product -> productRepository.save(product))
					.map(productentity -> modelMapper.map(productentity, ProductDTO.class))
					.doOnSubscribe(dto -> log.info("susscess"));
		}
	}
	public Mono<ProductDTO> finđById(Long productDTOID){
		return productRepository.findById(productDTOID).map(ProductDTO -> modelMapper.map(ProductDTO, ProductDTO.class)).switchIfEmpty(Mono.error(new CommonException("Product00", "Products is empty", HttpStatus.BAD_REQUEST)));
	}
}
