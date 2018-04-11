package tracker.mapping;

import org.springframework.stereotype.Component;

import tracker.dto.PersonDto;
import tracker.entity.PersonEntity;

@Component
public class PersonDtoToEntityMapper {
	
	public PersonEntity toPersonEntity(PersonDto dto) {
		return PersonEntity.builder()
			.name(dto.getName())
			.heightInCm(dto.getHeightInCm())
			.weightRecords(dto.getWeightRecords())
			.build();
	}
}
