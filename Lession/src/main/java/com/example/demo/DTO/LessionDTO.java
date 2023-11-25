package com.example.demo.DTO;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import com.example.demo.Entity.LessionEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@With
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessionDTO {
	private Long id;
	private String video;
	private String path;
	private String title;
	private String description;
	private Long productId;
	private String folder;
	private Long delete;
	private String date;
}