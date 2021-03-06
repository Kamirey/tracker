package tracker.dao;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tracker.entity.AbstractEntity;
import tracker.entity.PersonEntity;
import tracker.entity.WeightRecordEntity;

@Service @Primary
@Transactional
@Repository
public class TrackerDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void persist(AbstractEntity entity) {
		entityManager.persist(entity);
	}
	
	public void persist(AbstractEntity... entities) {
		Arrays.stream(entities).forEach(entity -> {
			entityManager.persist(entity);
		});
	}
	
	public void remove(AbstractEntity entity) {
		entityManager.remove(entity);
	}
	
	public void update(AbstractEntity entity) {
		entityManager.merge(entity);
	}

	public PersonEntity deletePerson(UUID personId) {
		PersonEntity person = getPersonById(personId);
		if (person == null) {
			return null;
		}
		entityManager.remove(person);
		return person;
	}
	
	public PersonEntity getPersonById(UUID personId) {
		TypedQuery<PersonEntity> query = entityManager.createQuery("SELECT p FROM PersonEntity p WHERE p.id=:personId", PersonEntity.class);
		query.setParameter("personId", personId);
		List<PersonEntity> resultList = query.getResultList();
		return resultList.isEmpty() ? null : resultList.get(0);
	}
	
	public List<PersonEntity> getPersons() {
		TypedQuery<PersonEntity> query = entityManager.createQuery("SELECT p FROM PersonEntity p", PersonEntity.class);
		return query.getResultList();
	}

	public List<WeightRecordEntity> getWeightRecordsForPerson(UUID personId) {
		TypedQuery<WeightRecordEntity> query = entityManager.createQuery("SELECT w FROM WeightRecordEntity w WHERE w.person.id=:personId", WeightRecordEntity.class);
		query.setParameter("personId", personId);
		return query.getResultList();
	}

	public WeightRecordEntity deleteWeightRecord(UUID weightRecordId) {
		TypedQuery<WeightRecordEntity> query = entityManager.createQuery("SELECT w FROM WeightRecordEntity w WHERE w.id=:weightRecordId", WeightRecordEntity.class);
		query.setParameter("weightRecordId", weightRecordId);
		List<WeightRecordEntity> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return null;
		}
		entityManager.remove(resultList.get(0));
		return resultList.get(0);
	}
}
