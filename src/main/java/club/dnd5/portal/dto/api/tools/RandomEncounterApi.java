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
	private Collection<NameValueApi> enviroments; 
	private Collection<NameValueApi> levels;

	private String description;
	private SourceApi source;
	public RandomEncounterApi(RandomEncounterRow encounter) {
		description = encounter.getDescription();
		source = new  SourceApi(encounter.getBook());
	}
	public RandomEncounterApi(HabitatType[] values) {
		levels = new ArrayList<>();
		levels.add(new NameValueApi("1-4", 1));
		levels.add(new NameValueApi("5-10", 2));
		levels.add(new NameValueApi("11-15", 3));
		levels.add(new NameValueApi("17-20", 4));
		enviroments = Arrays
				.stream(values)
				.map(e -> new NameValueApi(e.getName(), e.name()))
				.collect(Collectors.toList());
	}
}