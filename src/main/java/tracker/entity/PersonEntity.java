package tracker.entity;
import java.util.List;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode(of = {"id"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "person")
public class PersonEntity {
	
	@Id
	@GenericGenerator(name = "uuidgen", strategy = "tracker.entity.UuidGenerator")
	@GeneratedValue(generator="uuidgen")
	@Basic(optional = false)
    @Column(name = "id", unique=true, nullable = false)
	@Setter
	private UUID id;

	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "height_in_cm", nullable = false)
	private int heightInCm;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="person", targetEntity = WeightRecordEntity.class)
	private List<WeightRecordEntity> weightRecords;
}
