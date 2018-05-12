package tracker;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
public class WeightRecordDeleteEndpointTest {
		
	@Value("${local.server.port}")
	private int localServerPort;
	
	@Autowired
	private TrackerDao daoForSetup;
	
	@Before
	public void setup() {
		PersonEntity person1 = PersonEntity.builder().name("person 1").height(160).build();
		WeightRecordEntity record1 = WeightRecordEntity.builder().person(person1).timeStamp(new Date(200)).weight(80).build();
		WeightRecordEntity record2 = WeightRecordEntity.builder().person(person1).timeStamp(new Date(300)).weight(90).build();
		
		daoForSetup.persist(person1, record1, record2);
	}

	@Test
	public void whenDeleteWeightRecord_shouldDeleteRecord() {
		List<PersonEntity> persons = daoForSetup.getPersons();
		List<WeightRecordEntity> originalWeightRecords = daoForSetup.getWeightRecordsForPerson(persons.get(0).getId());
		
		given()
		.when()
			.delete("http://localhost:" + localServerPort + "/weightRecords/" + originalWeightRecords.get(0).getId())
		.then()
			.statusCode(HttpStatus.SC_NO_CONTENT)
		;
		
		List<WeightRecordEntity> weightRecords = daoForSetup.getWeightRecordsForPerson(persons.get(0).getId());
		assertFalse(weightRecords.contains(originalWeightRecords.get(0)));
		assertTrue(weightRecords.contains(originalWeightRecords.get(1)));
	}
	
	@Test
	public void whenDeleteWeightRecord_withRandomUuid_shouldReturn404() {
		given()
		.when()
			.delete("http://localhost:" + localServerPort + "/weightRecords/" + UUID.randomUUID())
		.then()
			.statusCode(HttpStatus.SC_NOT_FOUND)
		;
	}
	
	@Test
	public void whenDeleteWeightRecord_withNonUuid_shouldReturn400() {
		given()
		.when()
			.delete("http://localhost:" + localServerPort + "/weightRecords/nonUuid")
		.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
		;
	}
}
