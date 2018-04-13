package tracker;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import tracker.dao.TrackerDao;
import tracker.entity.PersonEntity;
import tracker.entity.WeightRecordEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackerApplication.class)
@TestPropertySource(locations="classpath:test.properties")
@WebAppConfiguration
@IntegrationTest
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
public class WeightRecordPostEndpointTest {
		
	@Value("${local.server.port}")
	private int localServerPort;

	@Autowired
	private TrackerDao daoForSetup;
	
	@Before
	public void setup() {
		PersonEntity person1 = PersonEntity.builder().name("person 1").heightInCm(160).build();
		PersonEntity person2 = PersonEntity.builder().name("person 2").heightInCm(180).build();
		
		daoForSetup.persist(person1);
		daoForSetup.persist(person2);
	}
	
	@Test
	public void whenPostWeightRecord_shouldAddWeightRecordToCorrectPerson() {
		PersonEntity person = daoForSetup.getPersons().get(0);
		
		assertEquals(0, person.getWeightRecords().size());
		
		given()
			.contentType("application/json")
			.body("{\"timeStamp\":\"200\",\"weightInKg\":\"80\"}")
		.when()
			.post("http://localhost:" + localServerPort + "/weightRecords/" + person.getId())
		.then()
			.statusCode(HttpStatus.SC_OK)
			.body("timeStamp", equalTo(200))
			.body("weightInKg", equalTo(80))
		;
		
		List<WeightRecordEntity> weightRecords = daoForSetup.getPersonById(person.getId()).getWeightRecords();
		assertEquals(1, weightRecords.size());
		assertEquals(person, weightRecords.get(0).getPerson());
		assertEquals(200, weightRecords.get(0).getTimeStamp().getTime());
		assertEquals(80, weightRecords.get(0).getWeightInKg());
	}
	
	
	@Test
	public void whenPostWeightRecord_withEmptyBody_shouldReturn400() {
		UUID personId = daoForSetup.getPersons().get(0).getId();
		
		given()
			.contentType("application/json")
		.when()
		.post("http://localhost:" + localServerPort + "/weightRecords/" + personId)
		.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
		;
	}
	
	@Test
	public void whenPostWeightRecord_withMalformedBody_shouldReturn400() {
		UUID personId = daoForSetup.getPersons().get(0).getId();
		
		given()
			.contentType("application/json")
			.body("{\"timeStamp\":\"200\",\"something\":\"80\"}")
		.when()
		.post("http://localhost:" + localServerPort + "/weightRecords/" + personId)
		.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
		;
	}
	
	
	@Test
	public void whenPostWeightRecord_withRandomUuid_shouldReturn404() {	
		given()
		.contentType("application/json")
		.body("{\"timeStamp\":\"200\",\"weightInKg\":\"80\"}")
	.when()
		.post("http://localhost:" + localServerPort + "/weightRecords/" + UUID.randomUUID())
		.then()
			.statusCode(HttpStatus.SC_NOT_FOUND)
		;
	}
	
	@Test
	public void whenPostWeightRecord_withNonUuid_shouldReturn400() {
		given()
		.contentType("application/json")
		.body("{\"timeStamp\":\"200\",\"weightInKg\":\"80\"}")
	.when()
		.post("http://localhost:" + localServerPort + "/weightRecords/nonUuid")
		.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
		;
	}
}
