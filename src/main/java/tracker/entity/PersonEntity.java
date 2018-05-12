package tracker.entity;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "person")
public class PersonEntity extends AbstractEntity {

	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "height", nullable = false)
	private int height;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="person", targetEntity = WeightRecordEntity.class)
	private List<WeightRecordEntity> weightRecords;
}
