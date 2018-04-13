package tracker;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TrackerApplication.class)
@TestPropertySource(locations="classpath:test.properties")
@WebAppConfiguration
@IntegrationTest
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_CLASS)
public class PersonsGetEndpointTest {
		
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
	public void whenGetPersons_shouldReturnAllPersons() {
		given()
		.when()
			.get("http://localhost:" + localServerPort + "/persons/")
		.then()
			.statusCode(HttpStatus.SC_OK)
			.body("results.size", equalTo(2))
			.body("results[0].name", equalTo("person 1"))
			.body("results[0].heightInCm", equalTo(160))
			.body("results[1].name", equalTo("person 2"))
			.body("results[1].heightInCm", equalTo(180))
		;
	}
	
	@Test
	public void whenGetPersonById_shouldReturnCorrectPerson() {
		List<PersonEntity> persons = daoForSetup.getPersons();
		
		given()
		.when()
			.get("http://localhost:" + localServerPort + "/persons/" + persons.get(0).getId())
		.then()
			.statusCode(HttpStatus.SC_OK)
			.body("name", equalTo("person 1"))
			.body("heightInCm", equalTo(160))
		;
	}
	
	@Test
	public void whenGetPersonById_withRandomUuid_shouldReturn404() {
		given()
		.when()
			.get("http://localhost:" + localServerPort + "/persons/" + UUID.randomUUID())
		.then()
			.statusCode(HttpStatus.SC_NOT_FOUND)
		;
	}
	
	@Test
	public void whenGetPersonById_withNonUuid_shouldReturn400() {
		given()
		.when()
			.get("http://localhost:" + localServerPort + "/persons/nonUuid")
		.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
		;
	}
}
