package club.dnd5.portal.dto.trait;

import java.util.stream.Collectors;

import org.thymeleaf.util.StringUtils;

import club.dnd5.portal.model.AbilityType;
import club.dnd5.portal.model.SkillType;
import club.dnd5.portal.model.trait.Trait;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TraitDto {
	private int id;
	private String name;
	private String englishName;
	private String requirement;
	private String abilities;
	private String skills;
	private String book;
	private String bookshort;
	
	public TraitDto(Trait trait) {
		id = trait.getId();
		name = StringUtils.capitalizeWords(trait.getName().toLowerCase())
				.replace(" И ", " и ").replace(" Или ", " или ").replace(" За ", " за ").replace(" С ", " с ").replace(" На ", " на ").replace(" От ", " от ").replace(" По ", " по ")
				.replace(" Над ", " над ").replace(" В ", " в ");
		englishName = trait.getEnglishName();
		requirement = trait.getRequirement();
		abilities = trait.getAbilities().stream().map(AbilityType::getCyrilicName).collect(Collectors.joining(", "));
		skills = trait.getSkills().stream().map(SkillType::getCyrilicName).collect(Collectors.joining(", "));
		book = trait.getBook().getName();
		bookshort = trait.getBook().getSource();
	}
}