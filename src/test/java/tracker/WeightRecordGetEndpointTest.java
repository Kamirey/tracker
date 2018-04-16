package tracker;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.Date;
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
public class WeightRecordGetEndpointTest {
		
	@Value("${local.server.port}")
	private int localServerPort;
	
	@Autowired
	private TrackerDao daoForSetup;
	
	@Before
	public void setup() {
		PersonEntity person1 = PersonEntity.builder().name("person 1").heightInCm(160).build();
		PersonEntity person2 = PersonEntity.builder().name("person 2").heightInCm(180).build();
		WeightRecordEntity record1 = WeightRecordEntity.builder().person(person1).timeStamp(new Date(200)).weightInKg(80).build();
		WeightRecordEntity record2 = WeightRecordEntity.builder().person(person1).timeStamp(new Date(300)).weightInKg(90).build();
		
		daoForSetup.persist(person1, person2, record1, record2);
	}

	@Test
	public void whenGetWeightRecords_shouldReturnAllWeightRecordsForPerson() {
		List<PersonEntity> persons = daoForSetup.getPersons();
		
		given()
		.when()
			.get("http://localhost:" + localServerPort + "/weightRecords/" + persons.get(0).getId())
		.then()
			.statusCode(HttpStatus.SC_OK)
			.body("results.size", equalTo(2))
			.body("results[0].timeStamp", equalTo(200))
			.body("results[0].weightInKg", equalTo(80))
			.body("results[1].timeStamp", equalTo(300))
			.body("results[1].weightInKg", equalTo(90))
		;
	}
	
	@Test
	public void whenGetWeightRecords_withRandomUuid_shouldReturn404() {
		given()
		.when()
			.get("http://localhost:" + localServerPort + "/weightRecords/" + UUID.randomUUID())
		.then()
			.statusCode(HttpStatus.SC_NOT_FOUND)
		;
	}
	
	@Test
	public void whenGetWeightRecords_forPersonWithoutRecords_shouldReturn404() {
		List<PersonEntity> persons = daoForSetup.getPersons();
		
		given()
		.when()
			.get("http://localhost:" + localServerPort + "/weightRecords/" + persons.get(1).getId())
		.then()
			.statusCode(HttpStatus.SC_NOT_FOUND)
		;
	}
	
	@Test
	public void whenGetWeightRecords_withNonUuid_shouldReturn400() {
		given()
		.when()
			.get("http://localhost:" + localServerPort + "/weightRecords/nonUuid")
		.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
		;
	}
}
