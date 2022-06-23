package club.dnd5.portal.dto.api.tools;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.model.encounters.RandomEncounterRow;
import club.dnd5.portal.model.encounters.RandomEncounterеTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@NoArgsConstructor
@Getter
@Setter
public class RandomEncounterTableApi {
	private String name;
	private String[] thead = new String[2];
	private Collection<Collection<String>> tbody = new ArrayList<>(100);
	
	public RandomEncounterTableApi(RandomEncounterеTable table) {
		name = table.getName();
		thead[0] = String.format("<dice-roller formula=\"%s\">%s</dice-roller>", table.getFormula(), table.getFormula());
		thead[1] = "Столкновение";
		for (RandomEncounterRow reRow : table.getEncounters()) {
			Collection<String> row = new ArrayList<>(2);
			row.add(reRow.getEnd() == reRow.getStart() ? String.valueOf(reRow.getStart()) : String.format("%d-%d", reRow.getEnd(), reRow.getStart()));
			row.add(reRow.getDescription());
			tbody.add(row);
		}
	}
}