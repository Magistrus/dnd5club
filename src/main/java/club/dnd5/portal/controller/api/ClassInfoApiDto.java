package club.dnd5.portal.controller.api;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import club.dnd5.portal.dto.api.classes.NameApiDto;
import club.dnd5.portal.model.SpellcasterType;
import club.dnd5.portal.model.classes.HeroClass;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)

@Getter
@Setter
public class ClassInfoApiDto {
	private NameApiDto name;
	private Collection<ClassTabApiDto> tabs = new ArrayList<>(5);
	private Collection<String> images;

	public ClassInfoApiDto(HeroClass heroClass, Collection<String> images) {
		name = new NameApiDto(heroClass.getCapitalazeName(), heroClass.getEnglishName());
		this.images = images;
		tabs.add(new ClassTabApiDto("Навыки", String.format("/classes/%s/traits", heroClass.getUrlName()), "tab-traits", 0));
		tabs.add(new ClassTabApiDto("Описание", String.format("/classes/%s/description", heroClass.getUrlName()), "tab_description", 1));
		if (heroClass.getSpellcasterType() != SpellcasterType.NONE) {
			tabs.add(new ClassTabApiDto("Заклинания", String.format("/classes/%s/description", heroClass.getUrlName()), "tab-spells", 2));	
		}
		if (heroClass.getOptionType() != null) {
			tabs.add(new ClassTabApiDto(heroClass.getOptionType().getDisplayName(), String.format("/classes/%s/options", heroClass.getUrlName()), "tab-option", 3));
		}
	}
}