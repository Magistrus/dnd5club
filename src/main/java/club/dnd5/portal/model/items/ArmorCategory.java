package club.dnd5.portal.model.items;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArmorCategory {
	LIGHT("Легкий доспех"),
	MEDIUM("Средний доспех"),
	HEAVY("Тяжелый доспех"),
	SHIELD("Щит");
	private String name;
}