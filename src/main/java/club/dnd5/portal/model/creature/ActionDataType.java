package club.dnd5.portal.model.creature;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActionDataType {
	MELE_WEAPON("mwak", "Рукопашная атака оружием"),
	RANGE_WEAPON("rwak", "Дальнобойная атака оружием"),
	MELE_SPELL("msak", "Рукопашная атака заклинанием"),
	RANGE_SPELL("rsak", "Дальнобойная атака заклинанием"),
	SAVING_THROW ("save", "спасбросок"),
	HEAL("h", "восстановл"), 
	TEMP_HEAL("th", "временных хитов");

	private String shortName;
	private String name;
	
	public static String parse(String text) {
		for (ActionDataType type : values()) {
			if (text.contains(type.name)) {
				return type.shortName;
			}
		}
		return "";
	}
}