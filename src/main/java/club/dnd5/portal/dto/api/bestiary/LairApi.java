package club.dnd5.portal.dto.api.bestiary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.creature.Lair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class LairApi {
	private String discription;
	private String action;
	private String effect;
	public LairApi(Lair lair){
		discription = lair.getDescription();
		action = lair.getAction();
		effect = lair.getEffect();
	}
}