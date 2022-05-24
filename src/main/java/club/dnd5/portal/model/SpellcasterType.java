package club.dnd5.portal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SpellcasterType {
	FULL(9),
	HALF(5),
	PARTLY(4),
	NONE(0);

	private int maxSpellLevel;
}