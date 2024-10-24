package com.example.demo.interfaces;

import java.util.List;

import com.example.demo.domain.ClientsDto;
import com.example.demo.domain.AdvancedSearchDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.ParameterErrorException;

public interface IClientService {

	public ClientsDto saveClient(ClientsDto client) throws ParameterErrorException;

	public ClientsDto findBysharedKey(String sharedKey) throws NotFoundException;

	public List<ClientsDto> getall();

	public List<ClientsDto> advancedSearch(AdvancedSearchDto criterial) throws ParameterErrorException, Exception;
}
