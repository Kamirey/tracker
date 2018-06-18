package tracker.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class NameId {
	@NonNull private UUID id;
	@NonNull private String name;
}