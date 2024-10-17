package com.example.tellmystory.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailsDto {

	private Integer postId;

	private String title;

	private String description;

	private String email;

	private Integer userId;

	private String userName;

	private String status;

	private String category;

	private Integer publisherId;

	private String publisherName;

	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate date;

}
