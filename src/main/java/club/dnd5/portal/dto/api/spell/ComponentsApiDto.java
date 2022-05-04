package club.dnd5.portal.dto.api.spell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.splells.Spell;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor

@Getter
@Setter
public class ComponentsApiDto {
	private Boolean v;
	private Boolean s;
	private Boolean m;
	public ComponentsApiDto(Spell spell) {
		
	}
}