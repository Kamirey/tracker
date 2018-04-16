package tracker;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
	
	@RequestMapping(value="/{personId}", method=RequestMethod.POST)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<WeightRecordEntity> addWeightRecord(@PathVariable("personId") UUID personId, @RequestBody WeightRecordDto dto) {
		PersonEntity person = dao.getPersonById(personId);
		if (person == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		WeightRecordEntity entity = mapper.toWeightRecordEntity(dto, person);
		if (entity == null) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
		dao.persist(entity);
		return new ResponseEntity<>(entity, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{personId}", method=RequestMethod.GET)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<Map<String, List<WeightRecordEntity>>> getWeightRecordsForPerson(@PathVariable("personId") UUID personId) {
		List<WeightRecordEntity> weightRecords = dao.getWeightRecordsForPerson(personId);
		if (weightRecords.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(ImmutableMap.of("results", weightRecords), HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseEntity<WeightRecordEntity> deleteWeightRecordById(@PathVariable("id") UUID weightRecordId) {
		WeightRecordEntity weightRecordEntity = dao.deleteWeightRecord(weightRecordId);
		if (weightRecordEntity == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
}
