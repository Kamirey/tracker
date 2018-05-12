package tracker.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class NameId {
	@NonNull private UUID id;
	@NonNull private String name;
}