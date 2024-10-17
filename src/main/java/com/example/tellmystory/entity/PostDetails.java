package com.example.tellmystory.entity;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "postId", scope = PostDetails.class)
public class PostDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postId;

	private String title;

	private String description;

	private String status;

	private String category;

	@JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate date;

	@ManyToOne(cascade = CascadeType.ALL)
	private AccountDetails accountUser;

	@ManyToOne(cascade = CascadeType.ALL)
	private AccountDetails accountPublisher;

}
