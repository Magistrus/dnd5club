package club.dnd5.portal.model.items;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TreasureType {
	COINS("Монеты"),
	GEM("Драгоценные камни"),
	WORKS_OF_ART("Произведения искусства"),
	BAUBLE("Безделушка"),
	GOTIC_BAUBLE("Готическая безделушка"),
	ELVEN_TRINKET("Безделушки из Царства фей"),
	AI_TRINKET("Безделушки Коорпорации приобретений"), 
	ELVEN1_TRINKET("Эльфийские безделушки"),
	EET_TRINKET("Безделушки Элементального зла"),
	IDR_TRINKET("Безделушки из Долины ледяного ветра"),
	;
	
	private String name;

	public static TreasureType parse(String type) {
		return Arrays.stream(values())
				.filter(t -> t.getName().equals(type))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
}