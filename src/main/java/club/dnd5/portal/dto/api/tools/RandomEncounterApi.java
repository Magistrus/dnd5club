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

	private String tableName;
	
	private String description;
	private SourceApi source;

	public RandomEncounterApi(RandomEncounterRow encounter) {
		tableName = encounter.getEncounter().getName();
		description = encounter.getDescription();
		source = new  SourceApi(encounter.getEncounter().getBook());
	}
}