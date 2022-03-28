package club.dnd5.portal.dto.api.spells;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntriesHigherLevel {
	private String type = "entries";
	private String name = "At Higher Levels";
	private List<String> entries;
	public EntriesHigherLevel(String upperLevel) {
		entries = Arrays.stream(upperLevel.replace("<p>", "").split("</p>"))
					.map(t->t.replace("\\\"", ""))
					.filter(t -> !t.isEmpty())
					.collect(Collectors.toList());
	}	
}