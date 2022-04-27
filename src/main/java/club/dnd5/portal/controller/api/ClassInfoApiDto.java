package club.dnd5.portal.controller.api;

import java.util.Collection;

import club.dnd5.portal.dto.api.classes.NameApiDto;
import club.dnd5.portal.model.SpellcasterType;
import club.dnd5.portal.model.classes.HeroClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassInfoApiDto {
	private NameApiDto name;
	private boolean spellcaster;
	private String optionName;
	private Collection<String> images;
	public ClassInfoApiDto(HeroClass heroClass, Collection<String> images) {
		name = new NameApiDto(heroClass.getCapitalazeName(), heroClass.getEnglishName());
		spellcaster = heroClass.getSpellcasterType() != SpellcasterType.NONE;
		optionName = heroClass.getOptionType().getDisplayName();
		this.images = images;
	}
}