package tracker;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tracker.dao.TrackerDao;
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
	
	@POST
	@RequestMapping("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPerson(@RequestBody PersonDto dto) {
		PersonEntity entity = mapper.toPersonEntity(dto);
		dao.persist(entity);
		return Response.status(Status.CREATED).entity(entity).build();
	}
	
	@GET
	@RequestMapping("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPersonById(@PathVariable("id") UUID personId) {
		PersonEntity personEntity = dao.getPersonById(personId);
		return Response.status(Status.OK).entity(personEntity).build();
	}
	
	@DELETE
	@RequestMapping("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deletePersonById(@PathVariable("id") UUID personId) {
		PersonEntity personEntity = dao.deletePerson(personId);
		if (personEntity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.status(Status.NO_CONTENT).build();
	}
	
	@PUT
	@RequestMapping("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePersonById(@PathVariable("id") UUID personId, @RequestBody PersonDto dto) {
		PersonEntity entity = mapper.toPersonEntity(dto);
		entity.setId(personId);
		dao.updatePerson(entity);
		return Response.status(Status.OK).entity(entity).build();
	}
}
