package club.dnd5.portal.model;

import java.util.EnumSet;
import java.util.Set;

import lombok.Getter;

@Getter
public enum Alignment {
	LAWFUL_GOOD("ЗД", "законно-добрый", "законно-добрая", "законно-доброе"), //0 
	LAWFUL_NEUTRAL("ЗН", "законно-нейтральный", "законно-нейтральная", "законно-нейтральное"), // 1
	LAWFUL_EVIL("ЗЗ", "законно-злой","законно-злая", "законно-злое"), //2 
	TRUE_NEUTRAL("ЗН", "законно-нейтральный", "законно-нейтральная","законно-нейтральное"), //3
	NEUTRAL_GOOD("НД", "нейтрально-добрый", "нейтрально-добрая", "нейтрально-доброе"), // 4
	NEUTRAL_EVIL("НЗ", "нейтрально-злой", "нейтрально-злая", "нейтрально-злое"), //5 
	CHAOTIC_GOOD("ХД", "хаотично-добрый", "хаотично-добрая","хаотично-доброе"), //6
	CHAOTIC_NEUTRAL("ХН", "хаотично-нейтральный",  "хаотично-нейтральная",  "хаотично-нейтральное"), //7
	CHAOTIC_EVIL("ХЗ", "хаотично-злой", "хаотично-злая", "хаотично-злое"), // 8
	NEUTRAL("Н", "нейтральный",  "нейтральная", "нейтральное"), //9
	WITHOUT("", "без мировоззрения", "без мировоззрения", "без мировоззрения"), // 10
	ALL_EVIL("", "любое злое мировоззрение", "любое злое мировоззрение", "любое злое мировоззрение"), // 11
	ALL("", "любое мировоззрение", "любое мировоззрение", "любое мировоззрение"), // 12
	NO_GOOD("", "любое не доброе мировоззрение", "любое не доброе мировоззрение", "любое не доброе мировоззрение") // 13
	;
	private String shortName;
	private String[] names;
	
	Alignment(String shortName, String... names){
		this.shortName = shortName;
		this.names = names;
	}

	public String getCyrilicName() {
		return names[0];
	}

	public static Alignment parse(String alignment) {
		if (alignment.equals("нейтральный")) {
			return NEUTRAL;
		}
		if (alignment.contains("-зл")) {
			if (alignment.contains("хаот")) {
				return Alignment.CHAOTIC_EVIL;
			}
			if (alignment.contains("закон")) {
				return LAWFUL_EVIL;
			}
			if (alignment.contains("нейтр")) {
				return NEUTRAL_EVIL;
			}
		} else if (alignment.contains("-добр")) {
			if (alignment.contains("нейтр")) {
				return Alignment.NEUTRAL_GOOD;
			}
			if (alignment.contains("хаот")) {
				return Alignment.CHAOTIC_GOOD;
			}
			if (alignment.contains("закон")) {
				return Alignment.LAWFUL_GOOD;
			}
		} else if (alignment.contains("-нейтр")) {
			if (alignment.contains("закон")) {
				return Alignment.LAWFUL_NEUTRAL;
			}
			if (alignment.contains("хаот")) {
				return Alignment.CHAOTIC_NEUTRAL;
			}
		}
		return WITHOUT;
	}

	public String getName(CreatureType type) {
		switch (type) {
		case ABERRATION:
		case FEY:
		case OOZE:
		case UNDEAD:
		case SLIME:
		case SMALL_BEAST:
			return names[1];
		case FIEND:
		case MONSTROSITY:
		case PLANT:
			return names[2];
		default:
			return names[0];
		}
	}
	
	public static Set<Alignment> getGods(){
		return EnumSet.of(LAWFUL_GOOD, LAWFUL_NEUTRAL, LAWFUL_EVIL, TRUE_NEUTRAL, NEUTRAL_GOOD, NEUTRAL_EVIL, CHAOTIC_GOOD, CHAOTIC_NEUTRAL, CHAOTIC_EVIL, NEUTRAL);
	}
}