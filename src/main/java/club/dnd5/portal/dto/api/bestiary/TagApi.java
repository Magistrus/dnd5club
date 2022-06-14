package club.dnd5.portal.dto.api.bestiary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.creature.CreatureRace;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class TagApi {
	private String name;
	private String description;
	public TagApi(CreatureRace race) {
		name = race.getName();
		description = race.getDescription();
	}
}