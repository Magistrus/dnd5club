package club.dnd5.portal.dto.api.tools;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.SourceApi;
import club.dnd5.portal.model.encounters.RandomEncounterRow;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class RandomEncounterApi {
	private String level;
	private String environment;
	
	private String description;
	private SourceApi source;

	public RandomEncounterApi(RandomEncounterRow encounter) {
		description = encounter.getDescription();
		source = new  SourceApi(encounter.getEncounter().getBook());
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
}