package club.dnd5.portal.model.items;

import java.util.Arrays;

import club.dnd5.portal.model.Dice;
import lombok.Getter;

@Getter
public enum Rarity {
	COMMON(100, "обычный", "обычная", "обычное"),
	UNCOMMON(400, "необычный", "необычная", "необычное"),
	RARE(4000, "редкий", "редкая", "редкое"),
	VERY_RARE(40_000, "очень редкий", "очень редкая", "очень редкое" ),
	LEGENDARY(200_000, "легендарный", "легендарная", "легендарное" ),
	ARTIFACT(1_500_000, "артефакт", "артефакт", "артефакт"),
	UNKNOWN(0, "не определеный", "не определеная", "не определеное");

	private String[] names;
	Rarity(int cost, String... names){
		baseCost = cost;
		this.names = names;
	}
	// базовая цена в золотых монетах
	private int baseCost;
	
	public static Rarity parse(String value) {
		return Arrays.stream(values()).filter(f -> f.getCyrilicName().equals(value)).findFirst().orElseThrow(IllegalArgumentException::new);
	}
	
	public String getCyrilicName() {
		return names[0];
	}
	public String getFemaleName() {
		return names[1];
	}
	public String getMiddleName() {
		return names[2];
	}
	
	public static int getCostDMG(Rarity rarity) {
		switch (rarity) {
		case COMMON:
			return Dice.roll(2, Dice.d6) * 10;
		case UNCOMMON:
			return Dice.roll(2, Dice.d6) * 100;
		case RARE:
			return Dice.roll(2, Dice.d10) * 1000;
		case VERY_RARE:
			return Dice.roll(2, Dice.d4) * 10000;
		case LEGENDARY:
			return Dice.roll(2, Dice.d6) * 25000;
		default:
			return 0;
		}
	}
}