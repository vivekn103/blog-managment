package com.example.tellmystory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailsDto {

	private Integer accountId;

	private String name;

	private String email;

	private String password;

	private String role;

}
