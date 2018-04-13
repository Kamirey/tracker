package tracker.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import tracker.entity.WeightRecordEntity;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class PersonDto {
	@NonNull private String name;
	private Integer heightInCm;
	@NonNull private List<WeightRecordEntity> weightRecords;
}
