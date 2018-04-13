package tracker.entity;

import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@ToString
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEntity {
	
	@Id
	@GenericGenerator(name = "uuidgen", strategy = "tracker.entity.UuidGenerator")
	@GeneratedValue(generator="uuidgen")
	@Basic(optional = false)
    @Column(name = "id", unique=true, nullable = false)
	@Setter
	private UUID id;
}
