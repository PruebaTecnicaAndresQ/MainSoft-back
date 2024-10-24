package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdvancedSearchDto {	
	
	private String name;
	private String email;
	private Long phone;
	private String starDate;
	private String endDate;

}
