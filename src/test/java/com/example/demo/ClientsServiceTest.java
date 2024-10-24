package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.demo.domain.AdvancedSearchDto;
import com.example.demo.domain.Clients;
import com.example.demo.domain.ClientsDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.ParameterErrorException;
import com.example.demo.interfaces.IClientService;
import com.example.demo.repository.ClientRepository;

@SpringBootTest
public class ClientsServiceTest {

	@Autowired
	private IClientService clientService;


	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void saveClienteTest() throws ParameterErrorException {
		ClientsDto mock = ClientsDto.builder().email("unitest1@prueba.com").dateAdded("23/10/2024").name("prueba")
				.phone(123456L).sharedKey("unitest1").build();
		String sharedKey = mock.getEmail().split("@")[0];
		ClientsDto mockResponse = clientService.saveClient(mock);
		assertNotNull(mockResponse);
		assertTrue(mockResponse.getSharedKey().equals(sharedKey));

	}

	@Test
	void saveClienteNullTest() throws ParameterErrorException {
		ClientsDto mock = null;
		ParameterErrorException ex = assertThrows(ParameterErrorException.class, () -> {
			clientService.saveClient(mock);
		});
		assertTrue(ex.getMessage().equals("datos de entrada nulos"));

	}

	@Test
	void saveClienteEmailNullTest() throws ParameterErrorException {
		ClientsDto mock = ClientsDto.builder().email(null).dateAdded("23/10/2024").name("prueba").phone(123456L)
				.sharedKey("unitest1").build();
		ParameterErrorException ex = assertThrows(ParameterErrorException.class, () -> {
			clientService.saveClient(mock);
		});
		assertTrue(ex.getMessage().equals("campo email nulo"));

	}

	@Test
	void findBySharedKeyTest() throws ParameterErrorException, NotFoundException {
		jdbcTemplate.execute("INSERT INTO CLIENTS (SHARED_KEY,NAME,EMAIL,PHONE,DATEADDED) VALUES ('PRUEBA450','PRUEBA1','PRUEBA1@PRUEBAS.COM.CO',123456789,'2024-10-23');");
		ClientsDto mockResponse = clientService.findBysharedKey("PRUEBA450");
		assertNotNull(mockResponse);
		assertTrue(mockResponse.getEmail().equals("PRUEBA1@PRUEBAS.COM.CO"));
	}

	@Test
	void findBySharedKeyNotFoundTest() throws ParameterErrorException, NotFoundException {
		NotFoundException ex = assertThrows(NotFoundException.class, () -> {
			clientService.findBysharedKey("unitest2");
		});
		assertTrue(ex.getMessage().equals("Cliente no encontrado"));
	}

	@Test
	void getAllTest() throws ParameterErrorException, NotFoundException {
		jdbcTemplate.execute("INSERT INTO CLIENTS (SHARED_KEY,NAME,EMAIL,PHONE,DATEADDED) VALUES ('PRUEBA1','PRUEBA1','PRUEBA1@PRUEBAS.COM.CO',123456789,'2024-10-23');");
		jdbcTemplate.execute("INSERT INTO CLIENTS (SHARED_KEY,NAME,EMAIL,PHONE,DATEADDED) VALUES ('PRUEBA2','PRUEBA2','PRUEBA2@PRUEBAS.COM.CO',123456789,'2024-10-23');");
		jdbcTemplate.execute("INSERT INTO CLIENTS (SHARED_KEY,NAME,EMAIL,PHONE,DATEADDED) VALUES ('PRUEBA3','PRUEBA3','PRUEBA3@PRUEBAS.COM.CO',123456789,'2024-10-23');");

		List<ClientsDto> mockResponse = clientService.getall();
		assertNotNull(mockResponse);
		assertTrue(mockResponse.size() == 3);
	}
	@Test
	void advanceSearchTest() throws Exception {
		jdbcTemplate.execute("INSERT INTO CLIENTS (SHARED_KEY,NAME,EMAIL,PHONE,DATEADDED) VALUES ('PRUEBA100','PRUEBA100','PRUEBA1@PRUEBAS.COM.CO',123456789,'2024-10-23');");
		AdvancedSearchDto criterial = AdvancedSearchDto.builder()
				.email("PRUEBA1@PRUEBAS.COM.CO")
				.name("PRUEBA100")
				.endDate("23/10/2024")
				.starDate("23/10/2024")
				.phone(123456789L)
				.build();
		List<ClientsDto> result = clientService.advancedSearch(criterial);
		assertTrue(result.size() == 1);
		
	}

}
