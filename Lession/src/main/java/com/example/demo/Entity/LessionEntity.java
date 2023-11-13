package com.example.demo.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Table(name = "lession")
@With
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessionEntity {
	@Id
	private Long id;
	private String video;
	private String path;
	private String title;
	private String description;

}