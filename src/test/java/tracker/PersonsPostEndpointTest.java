package tracker;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

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
public class PersonsPostEndpointTest {
		
	@Value("${local.server.port}")
	private int localServerPort;

	@Autowired
	private TrackerDao daoForSetup;
	
	@Before
	public void setup() {
		PersonEntity person1 = PersonEntity.builder().name("person 1").heightInCm(160).build();
		daoForSetup.persist(person1);
	}
	
	@Test
	public void whenPostPerson_shouldCreatePerson() {
		given()
			.contentType("application/json")
			.body("{\"name\":\"testName\",\"heightInCm\":\"80\"}")
		.when()
			.post("http://localhost:" + localServerPort + "/persons/")
		.then()
			.statusCode(HttpStatus.SC_CREATED)
			.body("name", equalTo("testName"))
			.body("heightInCm", equalTo(80))
		;
	}
	
	@Test
	public void whenPostPerson_withEmptyBody_shouldReturn400() {
		given()
			.contentType("application/json")
		.when()
			.post("http://localhost:" + localServerPort + "/persons/")
		.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
		;
	}
	
	@Test
	public void whenPostPerson_withMalformedBody_shouldReturn400() {
		given()
			.contentType("application/json")
			.body("{\"name\":\"testName\",\"something\":\"80\"}")
		.when()
			.post("http://localhost:" + localServerPort + "/persons/")
		.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
		;
	}
	
	@Test
	public void whenPutPerson_shouldUpdatePerson() {
		UUID personId = daoForSetup.getPersons().get(0).getId();
		
		given()
			.contentType("application/json")
			.body("{\"name\":\"updatedName\",\"heightInCm\":\"200\"}")
		.when()
			.put("http://localhost:" + localServerPort + "/persons/" + personId)
		.then()
			.statusCode(HttpStatus.SC_OK)
			.body("name", equalTo("updatedName"))
			.body("heightInCm", equalTo(200))
		;
	}
	
	@Test
	public void whenPutPerson_withEmptyBody_shouldReturn400() {
		UUID personId = daoForSetup.getPersons().get(0).getId();
		
		given()
			.contentType("application/json")
		.when()
			.put("http://localhost:" + localServerPort + "/persons/" + personId)
		.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
		;
	}
	
	@Test
	public void whenPutPerson_withMalformedBody_shouldReturn400() {
		UUID personId = daoForSetup.getPersons().get(0).getId();
		
		given()
			.contentType("application/json")
			.body("{\"name\":\"updatedName\",\"something\":\"80\"}")
		.when()
			.put("http://localhost:" + localServerPort + "/persons/" + personId)
		.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
		;
	}
}
