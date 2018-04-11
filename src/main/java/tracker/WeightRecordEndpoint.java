package tracker;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
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
import tracker.dto.WeightRecordDto;
import tracker.entity.PersonEntity;
import tracker.entity.WeightRecordEntity;
import tracker.mapping.WeightRecordDtoToEntityMapper;

@RestController
@RequestMapping("weightRecords/")
public class WeightRecordEndpoint {
	
	@Autowired
	private TrackerDao dao;
	
	@Autowired
	private WeightRecordDtoToEntityMapper mapper;
	
	@POST
	@RequestMapping("/{personId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addWeightRecord(@PathVariable("personId") UUID personId, @RequestBody WeightRecordDto dto) {
		PersonEntity person = dao.getPersonById(personId);
		WeightRecordEntity entity = mapper.toWeightRecordEntity(dto, person);
		dao.persist(entity);
		return Response.status(Status.CREATED).entity(entity).build();
	}

}
