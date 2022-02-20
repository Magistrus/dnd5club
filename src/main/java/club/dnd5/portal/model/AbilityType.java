package club.dnd5.portal.model;

import java.util.EnumSet;
import java.util.Set;

import org.thymeleaf.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Характеристики 
 */
@Getter
@AllArgsConstructor
public enum AbilityType {
	STRENGTH("Сила"),             // 0 
	DEXTERITY("Ловкость"),        // 1
	CONSTITUTION("Телосложение"), // 2
	INTELLIGENCE("Интеллект"),    // 3
	WISDOM("Мудрость"),           // 4
	CHARISMA("Харизма"),          // 5

	CHOICE("к другой"),
	CHOICE_UNIQUE("к двум другим");

	private String cyrilicName;

	public String getShortName() {
		if (this == WISDOM) {
			return "Мдр";
		}
		return cyrilicName.substring(0,3);
	}

	public static AbilityType parseShortName(String shortName) {

		switch (shortName) {
			case "Сил":
				return AbilityType.STRENGTH;
			case "Лов":
				return AbilityType.DEXTERITY;
			case "Тел":
				return AbilityType.CONSTITUTION;
			case "Инт":
				return AbilityType.INTELLIGENCE;
			case "Мдр":
			case "Муд":			
				return AbilityType.WISDOM;
			case "Хар":
				return AbilityType.CHARISMA;
		}
		return null;
	}
	
	public String getCapitalizeName() {
		return StringUtils.capitalize(name().toLowerCase());
	}

	public static AbilityType parse(String name) {
		for (AbilityType abilityType : values()) {
			if (abilityType.getCyrilicName().equalsIgnoreCase(name)) {
				return abilityType;
			}
		}
		return null;
	}

	public static byte getModifier(byte ability) {
		return (byte) ((ability - 10) < 0 ? (ability - 11) / 2 : (ability - 10) / 2);
	}
	public static Set<AbilityType> getBaseAbility(){
		return EnumSet.of(STRENGTH, DEXTERITY, CONSTITUTION, INTELLIGENCE, WISDOM, CHARISMA);
	}
}