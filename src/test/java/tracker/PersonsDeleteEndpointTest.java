package tracker;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertFalse;

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
public class PersonsDeleteEndpointTest {
		
	@Value("${local.server.port}")
	private int localServerPort;
	
	@Autowired
	private TrackerDao daoForSetup;
	
	@Before
	public void setup() {
		PersonEntity person1 = PersonEntity.builder().name("person 1").height(160).build();
		daoForSetup.persist(person1);
	}

	@Test
	public void whenDeletePerson_shouldDeletePerson() {
		List<PersonEntity> originalPersons = daoForSetup.getPersons();
		
		given()
		.when()
			.delete("http://localhost:" + localServerPort + "/persons/" + originalPersons.get(0).getId())
		.then()
			.statusCode(HttpStatus.SC_NO_CONTENT)
		;
		
		List<PersonEntity> persons = daoForSetup.getPersons();
		assertFalse(persons.contains(originalPersons.get(0)));
	}
	
	@Test
	public void whenDeletePerson_withRandomUuid_shouldReturn404() {
		given()
		.when()
			.delete("http://localhost:" + localServerPort + "/persons/" + UUID.randomUUID())
		.then()
			.statusCode(HttpStatus.SC_NOT_FOUND)
		;
	}
	
	@Test
	public void whenDeletePerson_withNonUuid_shouldReturn400() {
		given()
		.when()
			.delete("http://localhost:" + localServerPort + "/persons/nonUuid")
		.then()
			.statusCode(HttpStatus.SC_BAD_REQUEST)
		;
	}
}
