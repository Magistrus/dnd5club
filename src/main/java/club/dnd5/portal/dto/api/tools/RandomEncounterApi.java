package club.dnd5.portal.dto.api.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.NameValueApi;
import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.model.creature.HabitatType;
import club.dnd5.portal.model.encounters.RandomEncounterRow;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class RandomEncounterApi {
	private Collection<NameValueApi> environments; 
	private Collection<NameValueApi> levels;
	private String level;
	private String environment;
	
	private String description;
	private SourceApi source;
	public RandomEncounterApi(RandomEncounterRow encounter) {
		description = encounter.getDescription();
		source = new  SourceApi(encounter.getBook());
		switch (encounter.getEncounter().getLevel()) {
		case 1:
			level = "1-4";
			break;
		case 2:
			level = "5-7";
			break;
		case 3:
			level = "11-15";
			break;
		case 4:
			level = "17-20";
			break;
		}
		if (encounter.getEncounter().getType() != null) {
			environment = encounter.getEncounter().getType().getName();
		}
	}

	public RandomEncounterApi(HabitatType[] values) {
		levels = new ArrayList<>();
		levels.add(new NameValueApi("1-4", 1));
		levels.add(new NameValueApi("5-10", 2));
		levels.add(new NameValueApi("11-15", 3));
		levels.add(new NameValueApi("17-20", 4));
		environments = Arrays
				.stream(values)
				.map(e -> new NameValueApi(e.getName(), e.name()))
				.collect(Collectors.toList());
	}
}