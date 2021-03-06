package tracker;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jersey.repackaged.com.google.common.collect.ImmutableMap;
import tracker.dao.TrackerDao;
import tracker.dto.NameId;
import tracker.dto.PersonDto;
import tracker.entity.PersonEntity;
import tracker.mapping.PersonDtoToEntityMapper;

@RestController
@RequestMapping("persons")
public class PersonsEndpoint {
	
	@Autowired
	private TrackerDao dao;
	
	@Autowired
	private PersonDtoToEntityMapper mapper;
	
	@RequestMapping(value="/", method=RequestMethod.POST)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<PersonEntity> addPerson(@RequestBody PersonDto dto) {
		PersonEntity entity = mapper.toPersonEntity(dto);
		if (entity == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		dao.persist(entity);
		return new ResponseEntity<>(entity, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Map<String, List<NameId>>> getPersons() {
		List<PersonEntity> personEntities = dao.getPersons();
		List<NameId> personNameIds = personEntities.stream().map(person -> new NameId(person.getId(), person.getName())).collect(Collectors.toList());
		return new ResponseEntity<>(ImmutableMap.of("results", personNameIds), HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<PersonEntity> getPersonById(@PathVariable("id") UUID personId) {
		PersonEntity personEntity = dao.getPersonById(personId);
		if (personEntity == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(personEntity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<PersonEntity> deletePersonById(@PathVariable("id") UUID personId) {
		PersonEntity personEntity = dao.deletePerson(personId);
		if (personEntity == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<PersonEntity> updatePersonById(@PathVariable("id") UUID personId, @RequestBody PersonDto dto) {
		PersonEntity entity = mapper.toPersonEntity(dto);
		if (entity == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		entity.setId(personId);
		dao.update(entity);
		return new ResponseEntity<>(entity, HttpStatus.OK);
	}
}
