package club.dnd5.portal.model.creature;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActionDataType {
	MELE_WEAPON("mwak"),
	RANGE_WEAPON("rwak"),
	MELE_SPELL("msak"),
	RANGE_SPELL("rsak"),
	SAVING_THROW ("save"),
	HEAL("h"), 
	TEMP_HEAL("th");
	private String shortName;
}