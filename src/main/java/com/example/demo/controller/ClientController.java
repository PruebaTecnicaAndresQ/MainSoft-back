package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.ClientsDto;
import com.example.demo.domain.AdvancedSearchDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.ParameterErrorException;
import com.example.demo.interfaces.IClientService;

@RestController
@RequestMapping("/clients")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*",methods = {
        RequestMethod.GET,
        RequestMethod.POST
})
public class ClientController {

	@Autowired
	private IClientService clientService;

	@GetMapping(path = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ClientsDto>> getAll() {
		return ResponseEntity.ok(clientService.getall());
	}

	@GetMapping(path = "/findBySharedKey", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClientsDto> findBySharedKey(@RequestParam("sharedKey") String sharedKey)
			throws NotFoundException {
		return ResponseEntity.ok(clientService.findBysharedKey(sharedKey));
	}

	@PostMapping(path = "/saveClient", consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClientsDto> saveClient(@RequestBody ClientsDto client) throws ParameterErrorException {
		return ResponseEntity.ok(clientService.saveClient(client));
	}

	@PostMapping(path = "/advanteSearch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ClientsDto>> advanteSearch(@RequestBody AdvancedSearchDto criterial) throws ParameterErrorException, Exception {
		return ResponseEntity.ok(clientService.advancedSearch(criterial));
	}

}
