package club.dnd5.portal.dto.api.tools;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.encounters.RandomEncounterRow;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class RandomEncounterTableApi {
	private String[] thead = new String[2];

	public RandomEncounterTableApi(List<RandomEncounterRow> encounters) {
		
		//["<dice-roller formula=\"d12+d8\">d12+d8</dice-roller>", "Столкновение"],
	}
}