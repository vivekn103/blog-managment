package com.example.tellmystory.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "accountId", scope = AccountDetails.class)
public class AccountDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer accountId;

	private String name;

	private String email;

	private String password;

	private String role;// user or publisher

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "accountUser")
	List<PostDetails> postToUser;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "accountPublisher")
	List<PostDetails> postToPublisher;

}
