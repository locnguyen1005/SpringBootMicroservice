//package com.example.demo.Config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import com.example.demo.Entity.AccountEntity;
//import com.example.demo.Repository.AccountRepository;
//import com.google.common.base.Optional;
//
//public class CustomUserDetailsService implements UserDetailsService{
//
//	@Autowired
//	private AccountRepository repository;
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		java.util.Optional<AccountEntity>	credential = java.util.Optional.ofNullable(repository.findByEmail(username).block());
//        return credential.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("user not found with name :" + username));
//	}
//
//}
