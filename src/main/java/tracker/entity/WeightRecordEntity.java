package tracker.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode(of = {"id"})
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "history_weight")
public class WeightRecordEntity {
	
	@Id
	@GenericGenerator(name = "uuidgen", strategy = "tracker.entity.UuidGenerator")
	@GeneratedValue(generator="uuidgen")
	@Basic(optional = false)
    @Column(name = "id", unique=true, nullable = false)
	private UUID id;

	@Column(name = "timeStamp", nullable = false)
	private Date timeStamp;
	
	@Column(name = "weight_in_kg")
	private int weightInKg;
	
	@JsonIgnore
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "personid", nullable = false)
	private PersonEntity person;
}
