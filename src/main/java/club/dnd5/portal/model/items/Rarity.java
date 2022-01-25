package club.dnd5.portal.model.items;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum Rarity {
	COMMON(100, "обычный", "обычная", "обычное"),
	UNCOMMON(400, "необычный", "необычная", "необычное"),
	RARE(4000, "редкий", "редкая", "редкое"),
	VERY_RARE(40_000, "очень редкий", "очень редкая", "очень редкое" ),
	LEGENDARY(200_000, "легендарный", "легендарная", "легендарное" ),
	ARTIFACT(1_500_000, "артефакт");

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
}