package club.dnd5.portal.dto.classes;

import org.thymeleaf.util.StringUtils;

import club.dnd5.portal.model.classes.HeroClassTrait;
import club.dnd5.portal.model.classes.archetype.ArchetypeTrait;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClassFetureDto {
	private int id;
	private String name;
	private byte level;
	private byte order;
	private String type;
	private String child;
	private String optional;
	private String prefix;
	private String description;

	public ClassFetureDto(HeroClassTrait feature, String className) {
		id = feature.getId();
		name = StringUtils.capitalizeWords(feature.getName().toLowerCase()).replace(" И ", " и ")
				.replace(" Или ", " или ").replace(" За ", " за ").replace(" С ", " с ").replace(" На ", " на ")
				.replace(" От ", " от ").replace(" По ", " по ").replace(" Над ", " над ").replace(" В ", " в ");
		level = feature.getLevel();
		description = feature.getDescription();
		type = String.valueOf(feature.getLevel());
		switch (feature.getLevel()) {
		case 5:
		case 6:
		case 7:
		case 8:
			type += "-го уровня";
			break;
		default:
			type += "-й уровень";
			break;
		}
		prefix ="c";
		if (className.equalsIgnoreCase("Чародей")) {
			className = "Чародея";
		} else if (className.equalsIgnoreCase("Изобретатель")) {
			className = "Изобретателя";
		} else if (className.equalsIgnoreCase("Кровавый охотник")) {
			className = "Кровавого охотника";
		}
		else if (className.equalsIgnoreCase("Заклинатель Напарник")) {
			className = "Заклинателя Напарника";
		}
		else if (className.equalsIgnoreCase("Эксперт Напарник")) {
			className = "Эксперта Напарника";
		}
		else if (className.equalsIgnoreCase("Боец Напарник")) {
			className = "Бойца Напарника";
		}
		else {
			className += "а"; 
		}
		child = feature.getChild();
		type+= ", умение " + StringUtils.capitalizeWords(className.toLowerCase());
		order = 2;
	}

	public ClassFetureDto(ArchetypeTrait feature, String archetypeName) {
		id = feature.getId();
		name = StringUtils.capitalizeWords(feature.getName().toLowerCase()).replace(" И ", " и ")
				.replace(" Или ", " или ").replace(" За ", " за ").replace(" С ", " с ").replace(" На ", " на ")
				.replace(" От ", " от ").replace(" По ", " по ").replace(" Над ", " над ").replace(" В ", " в ");
		level = feature.getLevel();
		description = feature.getDescription();
		type = String.valueOf(feature.getLevel());
		switch (feature.getLevel()) {
		case 5:
		case 6:
		case 7:
		case 8:
			type += "-го уровня";
			break;
		default:
			type += "-й уровень";
			break;
		}
		child = feature.getChild();
		prefix ="a";
		type += ", умение " + archetypeName;
		order = 1;
	}
}