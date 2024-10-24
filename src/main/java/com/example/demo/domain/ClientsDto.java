package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientsDto {
	
	private String sharedKey;
	private String name;
	private String email;
	private Long phone;
	private String dateAdded;
}
