package com.example.demo.Model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
	private Long id;
	private String name;
	private Long accountid;
	private String folder;
	private LocalDateTime starttime;
	private Long maxuser;
}
