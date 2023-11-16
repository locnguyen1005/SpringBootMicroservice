package com.example.demo.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessionRegister {
	private String video;
	private String path;
	private String title;
	private String description;
	private Long productId;
}