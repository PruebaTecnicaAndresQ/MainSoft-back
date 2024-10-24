package com.example.demo.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.example.demo.domain.Clients;
import com.example.demo.domain.ClientsDto;
import com.example.demo.domain.AdvancedSearchDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.ParameterErrorException;
import com.example.demo.interfaces.IClientService;
import com.example.demo.repository.ClientRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClientService implements IClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private SimpleDateFormat dformat = new SimpleDateFormat("dd/MM/yyyy");

	/**
	 * Implementacion del metodo para crear un cliente
	 * 
	 * @param AdvancedSearchDto
	 */
	@Override
	public ClientsDto saveClient(ClientsDto client) throws ParameterErrorException {
		try {
			if (client == null) {
				throw new ParameterErrorException("datos de entrada nulos");
			}
			if (client.getEmail() == null) {
				throw new ParameterErrorException("campo email nulo");
			}
			String sharedKey = client.getEmail().split("@")[0];

			Clients clientData = Clients.builder().email(client.getEmail()).name(client.getName())
					.phone(client.getPhone()).sharedKey(sharedKey)
					.dateAdded(new java.sql.Date(new java.util.Date().getTime())).build();
			clientRepository.save(clientData);
			client.setSharedKey(sharedKey);
			client.setDateAdded(dformat.format(clientData.getDateAdded()));
			return client;
		} catch (ParameterErrorException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}

	}

	/**
	 * Implementacion del metodo para obtener cliente por llaveCompartida
	 * 
	 * @param AdvancedSearchDto
	 */
	@Override
	public ClientsDto findBysharedKey(String sharedKey) throws NotFoundException {
		try {
			Clients cls = clientRepository.findBysharedkey(sharedKey);
			if (cls == null) {
				throw new NotFoundException("Cliente no encontrado");
			}
			return ClientsDto.builder().dateAdded(dformat.format(cls.getDateAdded())).email(cls.getEmail())
					.name(cls.getName()).phone(cls.getPhone()).sharedKey(cls.getSharedKey()).build();
		} catch (NotFoundException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}

	}

	/**
	 * Implementacion del metodo para obtener toda la informacion de la bd
	 * 
	 * @param AdvancedSearchDto
	 */
	@Override
	public List<ClientsDto> getall() {
		try {
			List<ClientsDto> resp = new ArrayList<>();
			List<Clients> lst = clientRepository.findAll();
			for (Clients cls : lst) {
				ClientsDto obj = ClientsDto.builder().dateAdded(dformat.format(cls.getDateAdded()))
						.email(cls.getEmail()).name(cls.getName()).phone(cls.getPhone()).sharedKey(cls.getSharedKey())
						.build();
				resp.add(obj);
			}
			return resp;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}

	}

	/**
	 * Implementacion del metodo de consulta avanzada
	 * 
	 * @param AdvancedSearchDto
	 */
	@Override
	public List<ClientsDto> advancedSearch(AdvancedSearchDto criterial) throws Exception {
		try {
			String selectClause = "SELECT * FROM clients ";
			StringBuilder whereClause = new StringBuilder();
			HashMap<Integer, Object> param = new HashMap<>();
			int paramCount = 1;
			if (criterial == null) {
				throw new ParameterErrorException("datos de entrada nulos");
			}

			if (criterial.getName() != null && !criterial.getName().isEmpty()) {
				whereClause.append(" WHERE name = ?");
				param.put(paramCount, criterial.getName());
				paramCount++;
			}

			if (criterial.getEmail() != null && !criterial.getEmail().isEmpty()) {
				if (whereClause.isEmpty()) {
					whereClause.append(" WHERE email = ?");
				} else {
					whereClause.append(" AND email = ?");
				}
				param.put(paramCount, criterial.getEmail());
				paramCount++;

			}

			if (criterial.getPhone() != null && !criterial.getPhone().equals(0L)) {
				if (whereClause.isEmpty()) {
					whereClause.append(" WHERE phone = ?");
				} else {
					whereClause.append(" AND phone = ?");
				}
				param.put(paramCount, criterial.getPhone());
				paramCount++;
			}
			if (criterial.getStarDate() != null && !criterial.getStarDate().isEmpty()) {
				if (whereClause.isEmpty()) {
					whereClause.append(" WHERE dateAdded >= ?");
				} else {
					whereClause.append(" AND dateAdded >= ?");
				}

				param.put(paramCount, dformat.parse(criterial.getStarDate()));
				paramCount++;

			}
			if (criterial.getEndDate() != null && !criterial.getEndDate().isEmpty()) {
				if (whereClause.isEmpty()) {
					whereClause.append(" WHERE dateAdded <= ?");
				} else {
					whereClause.append(" AND dateAdded <= ?");
				}

				param.put(paramCount, dformat.parse(criterial.getEndDate()));
				paramCount++;
			}
			String query = selectClause + whereClause.toString() + " ORDER BY 1 DESC;";

			List<ClientsDto> dto = jdbcTemplate.query(query, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					for (Map.Entry<Integer, Object> entry : param.entrySet()) {
						Integer key = entry.getKey();
						Object value = entry.getValue();
						if (value instanceof Long) {
							ps.setLong(key, (Long) value);
						}
						if (value instanceof Date) {
							Date date = (Date) value;
							ps.setDate(key.intValue(), new java.sql.Date(date.getTime()));
						}
						if (value instanceof String) {
							ps.setString(key, value.toString());
						}

					}

				}
			}, new RowMapper<ClientsDto>() {
				@Override
				public ClientsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
					return ClientsDto.builder().dateAdded(dformat.format(rs.getDate("DATEADDED")))
							.email(rs.getString("EMAIL")).name(rs.getString("NAME")).phone(rs.getLong("PHONE"))
							.sharedKey(rs.getString("SHARED_KEY")).build();
				}
			});
			if (dto.isEmpty()) {
				throw new NotFoundException("La consulta no genero resultados");
			}
			return dto;
		} catch (NotFoundException e) {
			log.error(e.getMessage());
			throw e;
		} catch (ParameterErrorException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}

	}

}
