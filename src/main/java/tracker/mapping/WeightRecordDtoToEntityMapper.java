package tracker.mapping;

import org.springframework.stereotype.Component;

import tracker.dto.WeightRecordDto;
import tracker.entity.PersonEntity;
import tracker.entity.WeightRecordEntity;

@Component
public class WeightRecordDtoToEntityMapper {
	
	public WeightRecordEntity toWeightRecordEntity(WeightRecordDto dto, PersonEntity person) {
		if (!isValid(dto)) {
			return null;
		}
		return WeightRecordEntity.builder()
			.timeStamp(dto.getTimeStamp())
			.person(person)
			.weightInKg(dto.getWeightInKg())
			.build();
	}

	private boolean isValid(WeightRecordDto dto) {
		return dto.getTimeStamp() != null && dto.getWeightInKg() != null;
	}
}
